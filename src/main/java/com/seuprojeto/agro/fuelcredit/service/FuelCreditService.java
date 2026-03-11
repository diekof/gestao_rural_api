package com.seuprojeto.agro.fuelcredit.service;

import com.seuprojeto.agro.exception.ConflictException;
import com.seuprojeto.agro.exception.ForbiddenException;
import com.seuprojeto.agro.exception.NotFoundException;
import com.seuprojeto.agro.exception.UnauthorizedException;
import com.seuprojeto.agro.fuelcredit.domain.FuelCredit;
import com.seuprojeto.agro.fuelcredit.domain.FuelCreditStatus;
import com.seuprojeto.agro.fuelcredit.dto.FuelCreditCreateRequest;
import com.seuprojeto.agro.fuelcredit.dto.FuelCreditResponse;
import com.seuprojeto.agro.fuelcredit.dto.FuelCreditUpdateRequest;
import com.seuprojeto.agro.fuelcredit.mapper.FuelCreditMapper;
import com.seuprojeto.agro.fuelcredit.repository.FuelCreditRepository;
import com.seuprojeto.agro.fuelcredit.specification.FuelCreditSpecification;
import com.seuprojeto.agro.security.AuthenticatedUser;
import com.seuprojeto.agro.security.TenantContext;
import com.seuprojeto.agro.user.domain.User;
import com.seuprojeto.agro.user.repository.UserRepository;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FuelCreditService {

    private final FuelCreditRepository repository;
    private final UserRepository userRepository;
    private final FuelCreditMapper mapper;

    public FuelCreditService(FuelCreditRepository repository, UserRepository userRepository, FuelCreditMapper mapper) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Transactional
    public FuelCreditResponse create(FuelCreditCreateRequest request, UUID tenantIdRequest) {
        UUID tenantId = resolveTenantId(tenantIdRequest);
        User user = userRepository.findById(request.userId()).orElseThrow(() -> new NotFoundException("User not found"));
        if (user.getTenantId() == null || !user.getTenantId().equals(tenantId)) {
            throw new ConflictException("User must belong to the tenant");
        }
        if (repository.existsByTenantIdAndUserId(tenantId, user.getId())) {
            throw new ConflictException("Fuel credit already exists for this user");
        }
        BigDecimal initialBalance = determineInitialBalance(request);
        FuelCredit credit = mapper.toEntity(request, initialBalance);
        credit.setTenantId(tenantId);
        if (initialBalance.compareTo(BigDecimal.ZERO) > 0) {
            credit.setLastRechargeAt(OffsetDateTime.now());
        }
        FuelCredit saved = repository.save(credit);
        return mapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public FuelCreditResponse findById(UUID id) {
        return mapper.toResponse(getByAccess(id));
    }

    @Transactional(readOnly = true)
    public Page<FuelCreditResponse> list(UUID userId, FuelCreditStatus status, Pageable pageable) {
        AuthenticatedUser current = getCurrentUser();
        UUID tenantFilter = current.isSuperAdmin() ? null : current.tenantId();
        UUID userFilter = userId;
        if (!current.isSuperAdmin() && current.role() == com.seuprojeto.agro.common.Role.OPERATOR) {
            userFilter = current.userId();
        }
        return repository.findAll(FuelCreditSpecification.filter(tenantFilter, userFilter, status), pageable)
                .map(mapper::toResponse);
    }

    @Transactional
    public FuelCreditResponse update(UUID id, FuelCreditUpdateRequest request) {
        FuelCredit credit = getByAccess(id);
        if (request.creditLimit() != null) {
            applyCreditLimitUpdate(credit, request.creditLimit());
        }
        if (request.rechargeAmount() != null) {
            applyRecharge(credit, request.rechargeAmount());
        }
        if (request.status() != null) {
            credit.setStatus(request.status());
        }
        return mapper.toResponse(repository.save(credit));
    }

    private void applyCreditLimitUpdate(FuelCredit credit, BigDecimal newLimit) {
        if (newLimit.compareTo(BigDecimal.ZERO) < 0) {
            throw new ConflictException("Credit limit must be greater or equal to zero");
        }
        BigDecimal consumed = credit.getCreditLimit().subtract(credit.getBalance());
        if (newLimit.compareTo(consumed) < 0) {
            throw new ConflictException("New limit cannot be lower than the consumed amount");
        }
        BigDecimal maxBalance = newLimit.subtract(consumed);
        if (credit.getBalance().compareTo(maxBalance) > 0) {
            credit.setBalance(maxBalance);
        }
        credit.setCreditLimit(newLimit);
    }

    private void applyRecharge(FuelCredit credit, BigDecimal rechargeAmount) {
        if (rechargeAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ConflictException("Recharge amount must be positive");
        }
        BigDecimal newBalance = credit.getBalance().add(rechargeAmount);
        if (newBalance.compareTo(credit.getCreditLimit()) > 0) {
            throw new ConflictException("Recharge exceeds credit limit");
        }
        credit.setBalance(newBalance);
        credit.setLastRechargeAt(OffsetDateTime.now());
    }

    private BigDecimal determineInitialBalance(FuelCreditCreateRequest request) {
        BigDecimal balance = request.initialBalance() != null ? request.initialBalance() : request.creditLimit();
        if (balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new ConflictException("Initial balance must be greater or equal to zero");
        }
        if (balance.compareTo(request.creditLimit()) > 0) {
            throw new ConflictException("Initial balance cannot exceed credit limit");
        }
        return balance;
    }

    private FuelCredit getByAccess(UUID id) {
        AuthenticatedUser current = getCurrentUser();
        FuelCredit entity = repository.findById(id).orElseThrow(() -> new NotFoundException("Fuel credit not found"));
        enforceTenantAccess(current, entity.getTenantId());
        if (!current.isSuperAdmin()
                && current.role() == com.seuprojeto.agro.common.Role.OPERATOR
                && !entity.getUserId().equals(current.userId())) {
            throw new ForbiddenException("Operators can only access their own credit");
        }
        return entity;
    }

    private UUID resolveTenantId(UUID tenantIdRequest) {
        AuthenticatedUser current = getCurrentUser();
        if (current.isSuperAdmin()) {
            if (tenantIdRequest == null) {
                throw new ForbiddenException("tenantId is required for SUPER_ADMIN");
            }
            return tenantIdRequest;
        }
        return TenantContext.getTenantId();
    }

    private void enforceTenantAccess(AuthenticatedUser current, UUID tenantId) {
        if (current.isSuperAdmin()) {
            return;
        }
        if (!tenantId.equals(TenantContext.getTenantId())) {
            throw new ForbiddenException("Cross-tenant access denied");
        }
    }

    private AuthenticatedUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof AuthenticatedUser authUser)) {
            throw new UnauthorizedException("Unauthenticated");
        }
        return authUser;
    }
}


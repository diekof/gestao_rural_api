package com.seuprojeto.agro.fuelsupply.service;

import com.seuprojeto.agro.exception.ConflictException;
import com.seuprojeto.agro.exception.ForbiddenException;
import com.seuprojeto.agro.exception.NotFoundException;
import com.seuprojeto.agro.exception.UnauthorizedException;
import com.seuprojeto.agro.fuelcredit.domain.FuelCredit;
import com.seuprojeto.agro.fuelcredit.repository.FuelCreditRepository;
import com.seuprojeto.agro.fuelsupply.domain.FuelSupply;
import com.seuprojeto.agro.fuelsupply.dto.FuelSupplyCreateRequest;
import com.seuprojeto.agro.fuelsupply.dto.FuelSupplyResponse;
import com.seuprojeto.agro.fuelsupply.dto.FuelSupplyUpdateRequest;
import com.seuprojeto.agro.fuelsupply.mapper.FuelSupplyMapper;
import com.seuprojeto.agro.fuelsupply.repository.FuelSupplyRepository;
import com.seuprojeto.agro.fuelsupply.specification.FuelSupplySpecification;
import com.seuprojeto.agro.machine.domain.Machine;
import com.seuprojeto.agro.machine.repository.MachineRepository;
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
public class FuelSupplyService {

    private final FuelSupplyRepository repository;
    private final FuelCreditRepository creditRepository;
    private final MachineRepository machineRepository;
    private final UserRepository userRepository;
    private final FuelSupplyMapper mapper;

    public FuelSupplyService(FuelSupplyRepository repository, FuelCreditRepository creditRepository,
                             MachineRepository machineRepository, UserRepository userRepository,
                             FuelSupplyMapper mapper) {
        this.repository = repository;
        this.creditRepository = creditRepository;
        this.machineRepository = machineRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Transactional
    public FuelSupplyResponse create(FuelSupplyCreateRequest request, UUID tenantIdRequest) {
        AuthenticatedUser current = getCurrentUser();
        UUID workerId = resolveWorkerId(current, request.userId());
        User worker =
                userRepository.findById(workerId).orElseThrow(() -> new NotFoundException("User not found"));
        UUID tenantId = resolveTenantId(current, tenantIdRequest, worker);
        validateUserTenant(worker, tenantId);
        Machine machine = resolveMachine(request.machineId(), tenantId);
        FuelCredit credit = creditRepository.findByTenantIdAndUserId(tenantId, workerId)
                .orElseThrow(() -> new NotFoundException("Fuel credit not found for user"));
        if (credit.getStatus() != com.seuprojeto.agro.fuelcredit.domain.FuelCreditStatus.ACTIVE) {
            throw new ConflictException("Fuel credit is not active");
        }
        ensureBalance(credit, request.valor());

        FuelSupply entity = mapper.toEntity(request);
        entity.setTenantId(tenantId);
        entity.setCreditId(credit.getId());
        entity.setUserId(workerId);
        entity.setWorkerName(worker.getNome());
        entity.setMachineName(machine != null ? machine.getNome() : null);

        FuelSupply saved = repository.save(entity);
        debitCredit(credit, request.valor());
        return mapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public FuelSupplyResponse findById(UUID id) {
        return mapper.toResponse(getByAccess(id));
    }

    @Transactional(readOnly = true)
    public Page<FuelSupplyResponse> list(UUID userId, UUID machineId, OffsetDateTime dataInicio,
                                         OffsetDateTime dataFim, Pageable pageable) {
        AuthenticatedUser current = getCurrentUser();
        UUID tenantFilter = current.isSuperAdmin() ? null : current.tenantId();
        UUID userFilter = userId;
        if (!current.isSuperAdmin() && current.role() == com.seuprojeto.agro.common.Role.OPERATOR) {
            userFilter = current.userId();
        }
        return repository.findAll(FuelSupplySpecification.filter(tenantFilter, userFilter, machineId, dataInicio, dataFim), pageable)
                .map(mapper::toResponse);
    }

    @Transactional
    public FuelSupplyResponse update(UUID id, FuelSupplyUpdateRequest request) {
        FuelSupply entity = getByAccess(id);
        FuelCredit credit = creditRepository.findById(entity.getCreditId())
                .orElseThrow(() -> new NotFoundException("Fuel credit not found"));
        BigDecimal previousValue = entity.getValor();
        mapper.updateEntity(entity, request);
        Machine machine = resolveMachine(request.machineId(), entity.getTenantId());
        entity.setMachineName(machine != null ? machine.getNome() : null);
        FuelSupply saved = repository.save(entity);
        adjustCreditAfterUpdate(credit, previousValue, entity.getValor());
        return mapper.toResponse(saved);
    }

    @Transactional
    public void delete(UUID id) {
        FuelSupply entity = getByAccess(id);
        FuelCredit credit = creditRepository.findById(entity.getCreditId())
                .orElseThrow(() -> new NotFoundException("Fuel credit not found"));
        repository.delete(entity);
        credit.setBalance(credit.getBalance().add(entity.getValor()));
        creditRepository.save(credit);
    }

    private void adjustCreditAfterUpdate(FuelCredit credit, BigDecimal previousValue, BigDecimal newValue) {
        int comparison = newValue.compareTo(previousValue);
        if (comparison == 0) {
            return;
        }
        if (comparison > 0) {
            BigDecimal delta = newValue.subtract(previousValue);
            ensureBalance(credit, delta);
            debitCredit(credit, delta);
        } else {
            BigDecimal delta = previousValue.subtract(newValue);
            credit.setBalance(credit.getBalance().add(delta));
            creditRepository.save(credit);
        }
    }

    private void debitCredit(FuelCredit credit, BigDecimal amount) {
        credit.setBalance(credit.getBalance().subtract(amount));
        creditRepository.save(credit);
    }

    private void ensureBalance(FuelCredit credit, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ConflictException("Amount must be positive");
        }
        if (credit.getBalance().compareTo(amount) < 0) {
            throw new ConflictException("Insufficient fuel credit balance");
        }
    }

    private Machine resolveMachine(UUID machineId, UUID tenantId) {
        if (machineId == null) {
            return null;
        }
        Machine machine = machineRepository.findById(machineId)
                .orElseThrow(() -> new NotFoundException("Machine not found"));
        if (!machine.getTenantId().equals(tenantId)) {
            throw new ConflictException("Machine must belong to tenant");
        }
        return machine;
    }

    private void validateUserTenant(User user, UUID tenantId) {
        if (user.getTenantId() == null || !user.getTenantId().equals(tenantId)) {
            throw new ConflictException("User must belong to tenant");
        }
    }

    private UUID resolveWorkerId(AuthenticatedUser current, UUID requestedUserId) {
        if (current.role() == com.seuprojeto.agro.common.Role.OPERATOR) {
            if (requestedUserId != null && !requestedUserId.equals(current.userId())) {
                throw new ForbiddenException("Operators can only submit their own refuels");
            }
            return current.userId();
        }
        if (requestedUserId == null) {
            throw new ConflictException("userId is required for managers and admins");
        }
        return requestedUserId;
    }

    private FuelSupply getByAccess(UUID id) {
        AuthenticatedUser current = getCurrentUser();
        FuelSupply entity = repository.findById(id).orElseThrow(() -> new NotFoundException("Fuel supply not found"));
        enforceTenantAccess(current, entity.getTenantId());
        if (!current.isSuperAdmin()
                && current.role() == com.seuprojeto.agro.common.Role.OPERATOR
                && !entity.getUserId().equals(current.userId())) {
            throw new ForbiddenException("Operators can only access their own refuels");
        }
        return entity;
    }

    private UUID resolveTenantId(AuthenticatedUser current, UUID tenantIdRequest, User worker) {
        if (current.isSuperAdmin()) {
            if (tenantIdRequest != null) {
                return tenantIdRequest;
            }
            if (worker != null && worker.getTenantId() != null) {
                return worker.getTenantId();
            }
            throw new ForbiddenException("tenantId is required for SUPER_ADMIN");
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


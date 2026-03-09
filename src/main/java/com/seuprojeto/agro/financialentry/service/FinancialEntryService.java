package com.seuprojeto.agro.financialentry.service;

import com.seuprojeto.agro.agriculturaloperation.domain.AgriculturalOperation;
import com.seuprojeto.agro.agriculturaloperation.repository.AgriculturalOperationRepository;
import com.seuprojeto.agro.exception.ConflictException;
import com.seuprojeto.agro.exception.ForbiddenException;
import com.seuprojeto.agro.exception.NotFoundException;
import com.seuprojeto.agro.exception.UnauthorizedException;
import com.seuprojeto.agro.financialentry.domain.FinancialCategory;
import com.seuprojeto.agro.financialentry.domain.FinancialEntry;
import com.seuprojeto.agro.financialentry.domain.FinancialEntryStatus;
import com.seuprojeto.agro.financialentry.domain.FinancialEntryType;
import com.seuprojeto.agro.financialentry.dto.FinancialEntryCreateRequest;
import com.seuprojeto.agro.financialentry.dto.FinancialEntryResponse;
import com.seuprojeto.agro.financialentry.dto.FinancialEntryUpdateRequest;
import com.seuprojeto.agro.financialentry.mapper.FinancialEntryMapper;
import com.seuprojeto.agro.financialentry.repository.FinancialEntryRepository;
import com.seuprojeto.agro.financialentry.specification.FinancialEntrySpecification;
import com.seuprojeto.agro.season.domain.Season;
import com.seuprojeto.agro.season.repository.SeasonRepository;
import com.seuprojeto.agro.security.AuthenticatedUser;
import com.seuprojeto.agro.security.TenantContext;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FinancialEntryService {

    private final FinancialEntryRepository repository;
    private final SeasonRepository seasonRepository;
    private final AgriculturalOperationRepository operationRepository;
    private final FinancialEntryMapper mapper;

    public FinancialEntryService(FinancialEntryRepository repository, SeasonRepository seasonRepository,
                                 AgriculturalOperationRepository operationRepository, FinancialEntryMapper mapper) {
        this.repository = repository;
        this.seasonRepository = seasonRepository;
        this.operationRepository = operationRepository;
        this.mapper = mapper;
    }

    @Transactional
    public FinancialEntryResponse create(FinancialEntryCreateRequest request, UUID tenantIdRequest) {
        UUID tenantId = resolveTenantId(tenantIdRequest);
        validateTenantReferences(tenantId, request.seasonId(), request.operationId());
        validateDates(request.status(), request.dataLancamento(), request.dataVencimento(), request.dataPagamento());
        FinancialEntry entry = mapper.toEntity(request);
        entry.setTenantId(tenantId);
        return mapper.toResponse(repository.save(entry));
    }

    @Transactional(readOnly = true)
    public FinancialEntryResponse findById(UUID id) {
        return mapper.toResponse(getByAccess(id));
    }

    @Transactional(readOnly = true)
    public Page<FinancialEntryResponse> list(UUID seasonId, UUID operationId, FinancialEntryType tipo, FinancialCategory categoria,
                                             FinancialEntryStatus status, LocalDate dataInicio, LocalDate dataFim, Pageable pageable) {
        AuthenticatedUser current = getCurrentUser();
        UUID tenantFilter = current.isSuperAdmin() ? null : current.tenantId();
        return repository.findAll(FinancialEntrySpecification.filter(tenantFilter, seasonId, operationId, tipo, categoria, status, dataInicio, dataFim), pageable)
                .map(mapper::toResponse);
    }

    @Transactional
    public FinancialEntryResponse update(UUID id, FinancialEntryUpdateRequest request) {
        FinancialEntry entry = getByAccess(id);
        validateTenantReferences(entry.getTenantId(), request.seasonId(), request.operationId());
        validateDates(request.status(), request.dataLancamento(), request.dataVencimento(), request.dataPagamento());
        mapper.updateEntity(entry, request);
        return mapper.toResponse(repository.save(entry));
    }

    @Transactional
    public void delete(UUID id) {
        repository.delete(getByAccess(id));
    }

    private void validateTenantReferences(UUID tenantId, UUID seasonId, UUID operationId) {
        if (seasonId != null) {
            Season season = seasonRepository.findById(seasonId).orElseThrow(() -> new NotFoundException("Season not found"));
            if (!season.getTenantId().equals(tenantId)) throw new ConflictException("Season must belong to same tenant");
        }
        if (operationId != null) {
            AgriculturalOperation operation = operationRepository.findById(operationId).orElseThrow(() -> new NotFoundException("Operation not found"));
            if (!operation.getTenantId().equals(tenantId)) throw new ConflictException("Operation must belong to same tenant");
        }
    }

    private void validateDates(FinancialEntryStatus status, LocalDate dataLancamento, LocalDate dataVencimento, LocalDate dataPagamento) {
        if (dataVencimento != null && dataVencimento.isBefore(dataLancamento)) {
            throw new ConflictException("Due date cannot be before entry date");
        }
        if (dataPagamento != null && dataPagamento.isBefore(dataLancamento)) {
            throw new ConflictException("Payment date cannot be before entry date");
        }
        if (status == FinancialEntryStatus.PAID && dataPagamento == null) {
            throw new ConflictException("Paid entries must have payment date");
        }
    }

    private FinancialEntry getByAccess(UUID id) {
        FinancialEntry entry = repository.findById(id).orElseThrow(() -> new NotFoundException("Financial entry not found"));
        enforceTenantAccess(getCurrentUser(), entry.getTenantId());
        return entry;
    }

    private UUID resolveTenantId(UUID tenantIdRequest) {
        AuthenticatedUser current = getCurrentUser();
        if (current.isSuperAdmin()) {
            if (tenantIdRequest == null) throw new ForbiddenException("tenantId is required for SUPER_ADMIN");
            return tenantIdRequest;
        }
        return TenantContext.getTenantId();
    }

    private void enforceTenantAccess(AuthenticatedUser current, UUID tenantId) {
        if (current.isSuperAdmin()) return;
        if (!tenantId.equals(TenantContext.getTenantId())) throw new ForbiddenException("Cross-tenant access denied");
    }

    private AuthenticatedUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof AuthenticatedUser authUser)) {
            throw new UnauthorizedException("Unauthenticated");
        }
        return authUser;
    }
}

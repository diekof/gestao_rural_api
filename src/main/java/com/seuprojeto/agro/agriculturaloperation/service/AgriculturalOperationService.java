package com.seuprojeto.agro.agriculturaloperation.service;

import com.seuprojeto.agro.agriculturaloperation.domain.AgriculturalOperation;
import com.seuprojeto.agro.agriculturaloperation.domain.OperationStatus;
import com.seuprojeto.agro.agriculturaloperation.domain.OperationType;
import com.seuprojeto.agro.agriculturaloperation.dto.AgriculturalOperationCreateRequest;
import com.seuprojeto.agro.agriculturaloperation.dto.AgriculturalOperationResponse;
import com.seuprojeto.agro.agriculturaloperation.dto.AgriculturalOperationUpdateRequest;
import com.seuprojeto.agro.agriculturaloperation.mapper.AgriculturalOperationMapper;
import com.seuprojeto.agro.agriculturaloperation.repository.AgriculturalOperationRepository;
import com.seuprojeto.agro.agriculturaloperation.specification.AgriculturalOperationSpecification;
import com.seuprojeto.agro.exception.ConflictException;
import com.seuprojeto.agro.exception.ForbiddenException;
import com.seuprojeto.agro.exception.NotFoundException;
import com.seuprojeto.agro.exception.UnauthorizedException;
import com.seuprojeto.agro.field.domain.Field;
import com.seuprojeto.agro.field.repository.FieldRepository;
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
public class AgriculturalOperationService {

    private final AgriculturalOperationRepository repository;
    private final SeasonRepository seasonRepository;
    private final FieldRepository fieldRepository;
    private final AgriculturalOperationMapper mapper;

    public AgriculturalOperationService(AgriculturalOperationRepository repository, SeasonRepository seasonRepository,
                                        FieldRepository fieldRepository, AgriculturalOperationMapper mapper) {
        this.repository = repository;
        this.seasonRepository = seasonRepository;
        this.fieldRepository = fieldRepository;
        this.mapper = mapper;
    }

    @Transactional
    public AgriculturalOperationResponse create(AgriculturalOperationCreateRequest request, UUID tenantIdRequest) {
        UUID tenantId = resolveTenantId(tenantIdRequest);
        validateForeignKeys(tenantId, request.seasonId(), request.fieldId());
        if (repository.existsByTenantIdAndSeasonIdAndDataOperacaoAndTipo(tenantId, request.seasonId(), request.dataOperacao(), request.tipo())) {
            throw new ConflictException("Operation already exists for this season/date/type");
        }
        AgriculturalOperation op = mapper.toEntity(request);
        op.setTenantId(tenantId);
        return mapper.toResponse(repository.save(op));
    }

    @Transactional(readOnly = true)
    public AgriculturalOperationResponse findById(UUID id) {
        return mapper.toResponse(getByAccess(id));
    }

    @Transactional(readOnly = true)
    public Page<AgriculturalOperationResponse> list(UUID seasonId, UUID fieldId, OperationType tipo, OperationStatus status,
                                                    LocalDate dataInicio, LocalDate dataFim, Pageable pageable) {
        AuthenticatedUser current = getCurrentUser();
        UUID tenantFilter = current.isSuperAdmin() ? null : current.tenantId();
        return repository.findAll(AgriculturalOperationSpecification.filter(tenantFilter, seasonId, fieldId, tipo, status, dataInicio, dataFim), pageable)
                .map(mapper::toResponse);
    }

    @Transactional
    public AgriculturalOperationResponse update(UUID id, AgriculturalOperationUpdateRequest request) {
        AgriculturalOperation op = getByAccess(id);
        validateForeignKeys(op.getTenantId(), request.seasonId(), request.fieldId());
        if (repository.existsByTenantIdAndSeasonIdAndDataOperacaoAndTipoAndIdNot(op.getTenantId(), request.seasonId(), request.dataOperacao(), request.tipo(), id)) {
            throw new ConflictException("Operation already exists for this season/date/type");
        }
        mapper.updateEntity(op, request);
        return mapper.toResponse(repository.save(op));
    }

    @Transactional
    public void delete(UUID id) {
        repository.delete(getByAccess(id));
    }

    private void validateForeignKeys(UUID tenantId, UUID seasonId, UUID fieldId) {
        Season season = seasonRepository.findById(seasonId).orElseThrow(() -> new NotFoundException("Season not found"));
        Field field = fieldRepository.findById(fieldId).orElseThrow(() -> new NotFoundException("Field not found"));
        if (!season.getTenantId().equals(tenantId) || !field.getTenantId().equals(tenantId)) {
            throw new ConflictException("Season and field must belong to same tenant");
        }
        if (!season.getFieldId().equals(fieldId)) {
            throw new ConflictException("Operation field must match season field");
        }
    }

    private AgriculturalOperation getByAccess(UUID id) {
        AgriculturalOperation op = repository.findById(id).orElseThrow(() -> new NotFoundException("Operation not found"));
        enforceTenantAccess(getCurrentUser(), op.getTenantId());
        return op;
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

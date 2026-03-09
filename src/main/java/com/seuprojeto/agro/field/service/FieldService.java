package com.seuprojeto.agro.field.service;

import com.seuprojeto.agro.common.Status;
import com.seuprojeto.agro.exception.ConflictException;
import com.seuprojeto.agro.exception.ForbiddenException;
import com.seuprojeto.agro.exception.NotFoundException;
import com.seuprojeto.agro.exception.UnauthorizedException;
import com.seuprojeto.agro.farm.domain.Farm;
import com.seuprojeto.agro.farm.repository.FarmRepository;
import com.seuprojeto.agro.field.domain.Field;
import com.seuprojeto.agro.field.dto.FieldCreateRequest;
import com.seuprojeto.agro.field.dto.FieldResponse;
import com.seuprojeto.agro.field.dto.FieldUpdateRequest;
import com.seuprojeto.agro.field.mapper.FieldMapper;
import com.seuprojeto.agro.field.repository.FieldRepository;
import com.seuprojeto.agro.field.specification.FieldSpecification;
import com.seuprojeto.agro.security.AuthenticatedUser;
import com.seuprojeto.agro.security.TenantContext;
import com.seuprojeto.agro.season.repository.SeasonRepository;
import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FieldService {

    private final FieldRepository fieldRepository;
    private final FarmRepository farmRepository;
    private final SeasonRepository seasonRepository;
    private final FieldMapper fieldMapper;

    public FieldService(FieldRepository fieldRepository, FarmRepository farmRepository, SeasonRepository seasonRepository, FieldMapper fieldMapper) {
        this.fieldRepository = fieldRepository;
        this.farmRepository = farmRepository;
        this.seasonRepository = seasonRepository;
        this.fieldMapper = fieldMapper;
    }

    @Transactional
    public FieldResponse create(FieldCreateRequest request, UUID tenantIdRequest) {
        UUID tenantId = resolveTenantId(tenantIdRequest);
        Farm farm = getFarmFromTenant(request.farmId(), tenantId);
        validateFarmArea(farm, request.areaHectares(), null);
        if (request.codigo() != null && fieldRepository.existsByFarmIdAndCodigoIgnoreCase(farm.getId(), request.codigo())) {
            throw new ConflictException("Field code already exists for farm");
        }
        Field field = fieldMapper.toEntity(request);
        field.setTenantId(tenantId);
        return fieldMapper.toResponse(fieldRepository.save(field));
    }

    @Transactional(readOnly = true)
    public FieldResponse findById(UUID id) {
        return fieldMapper.toResponse(getByAccess(id));
    }

    @Transactional(readOnly = true)
    public Page<FieldResponse> list(UUID farmId, String nome, Status status, String tipoSolo, Pageable pageable) {
        AuthenticatedUser current = getCurrentUser();
        UUID tenantFilter = current.isSuperAdmin() ? null : current.tenantId();
        return fieldRepository.findAll(FieldSpecification.filter(tenantFilter, farmId, nome, status, tipoSolo), pageable)
                .map(fieldMapper::toResponse);
    }

    @Transactional
    public FieldResponse update(UUID id, FieldUpdateRequest request) {
        Field field = getByAccess(id);
        Farm farm = getFarmFromTenant(request.farmId(), field.getTenantId());
        validateFarmArea(farm, request.areaHectares(), id);
        if (request.codigo() != null && fieldRepository.existsByFarmIdAndCodigoIgnoreCaseAndIdNot(farm.getId(), request.codigo(), id)) {
            throw new ConflictException("Field code already exists for farm");
        }
        fieldMapper.updateEntity(field, request);
        return fieldMapper.toResponse(fieldRepository.save(field));
    }

    @Transactional
    public void activate(UUID id) { changeStatus(id, Status.ACTIVE); }

    @Transactional
    public void deactivate(UUID id) { changeStatus(id, Status.INACTIVE); }

    @Transactional
    public void delete(UUID id) {
        Field field = getByAccess(id);
        if (seasonRepository.existsByFieldId(field.getId())) {
            throw new ConflictException("Field has linked seasons and cannot be deleted");
        }
        fieldRepository.delete(field);
    }

    private void changeStatus(UUID id, Status status) {
        Field field = getByAccess(id);
        field.setStatus(status);
        fieldRepository.save(field);
    }

    private void validateFarmArea(Farm farm, BigDecimal newFieldArea, UUID fieldId) {
        BigDecimal used = fieldId == null
                ? fieldRepository.sumAreaByFarmIdAndStatus(farm.getId(), Status.ACTIVE)
                : fieldRepository.sumAreaByFarmIdAndStatusExcludingField(farm.getId(), Status.ACTIVE, fieldId);
        if (used.add(newFieldArea).compareTo(farm.getAreaTotalHectares()) > 0) {
            throw new ConflictException("Sum of active fields exceeds farm total area");
        }
    }

    private Farm getFarmFromTenant(UUID farmId, UUID tenantId) {
        Farm farm = farmRepository.findById(farmId).orElseThrow(() -> new NotFoundException("Farm not found"));
        if (!farm.getTenantId().equals(tenantId)) throw new ConflictException("Farm must belong to same tenant");
        return farm;
    }

    private Field getByAccess(UUID id) {
        Field field = fieldRepository.findById(id).orElseThrow(() -> new NotFoundException("Field not found"));
        enforceTenantAccess(getCurrentUser(), field.getTenantId());
        return field;
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

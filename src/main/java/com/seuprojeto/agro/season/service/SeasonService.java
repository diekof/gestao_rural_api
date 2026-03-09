package com.seuprojeto.agro.season.service;

import com.seuprojeto.agro.crop.domain.Crop;
import com.seuprojeto.agro.crop.repository.CropRepository;
import com.seuprojeto.agro.exception.ConflictException;
import com.seuprojeto.agro.exception.ForbiddenException;
import com.seuprojeto.agro.exception.NotFoundException;
import com.seuprojeto.agro.exception.UnauthorizedException;
import com.seuprojeto.agro.field.domain.Field;
import com.seuprojeto.agro.field.repository.FieldRepository;
import com.seuprojeto.agro.security.AuthenticatedUser;
import com.seuprojeto.agro.security.TenantContext;
import com.seuprojeto.agro.season.domain.Season;
import com.seuprojeto.agro.season.domain.SeasonStatus;
import com.seuprojeto.agro.season.dto.SeasonCreateRequest;
import com.seuprojeto.agro.season.dto.SeasonResponse;
import com.seuprojeto.agro.season.dto.SeasonStatusUpdateRequest;
import com.seuprojeto.agro.season.dto.SeasonUpdateRequest;
import com.seuprojeto.agro.season.mapper.SeasonMapper;
import com.seuprojeto.agro.season.repository.SeasonRepository;
import com.seuprojeto.agro.season.specification.SeasonSpecification;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SeasonService {

    private final SeasonRepository seasonRepository;
    private final FieldRepository fieldRepository;
    private final CropRepository cropRepository;
    private final SeasonMapper seasonMapper;

    public SeasonService(SeasonRepository seasonRepository, FieldRepository fieldRepository, CropRepository cropRepository, SeasonMapper seasonMapper) {
        this.seasonRepository = seasonRepository;
        this.fieldRepository = fieldRepository;
        this.cropRepository = cropRepository;
        this.seasonMapper = seasonMapper;
    }

    @Transactional
    public SeasonResponse create(SeasonCreateRequest request, UUID tenantIdRequest) {
        UUID tenantId = resolveTenantId(tenantIdRequest);
        validateDates(request.dataInicio(), request.dataFim());
        Field field = findFieldFromTenant(request.fieldId(), tenantId);
        findCropFromTenant(request.cropId(), tenantId);
        validateArea(field, request.areaPlantada());
        validateUnique(tenantId, request.fieldId(), request.nome(), request.anoSafra(), null);

        Season season = seasonMapper.toEntity(request);
        season.setTenantId(tenantId);
        return seasonMapper.toResponse(seasonRepository.save(season));
    }

    @Transactional(readOnly = true)
    public SeasonResponse findById(UUID id) {
        return seasonMapper.toResponse(getByAccess(id));
    }

    @Transactional(readOnly = true)
    public Page<SeasonResponse> list(UUID fieldId, UUID cropId, Integer anoSafra, SeasonStatus status,
                                     java.time.LocalDate dataInicioFrom, java.time.LocalDate dataInicioTo, Pageable pageable) {
        AuthenticatedUser current = getCurrentUser();
        UUID tenantFilter = current.isSuperAdmin() ? null : current.tenantId();
        return seasonRepository.findAll(SeasonSpecification.filter(tenantFilter, fieldId, cropId, anoSafra, status, dataInicioFrom, dataInicioTo), pageable)
                .map(seasonMapper::toResponse);
    }

    @Transactional
    public SeasonResponse update(UUID id, SeasonUpdateRequest request) {
        Season season = getByAccess(id);
        validateDates(request.dataInicio(), request.dataFim());
        Field field = findFieldFromTenant(request.fieldId(), season.getTenantId());
        findCropFromTenant(request.cropId(), season.getTenantId());
        validateArea(field, request.areaPlantada());
        validateUnique(season.getTenantId(), request.fieldId(), request.nome(), request.anoSafra(), id);

        seasonMapper.updateEntity(season, request);
        return seasonMapper.toResponse(seasonRepository.save(season));
    }

    @Transactional
    public SeasonResponse updateStatus(UUID id, SeasonStatusUpdateRequest request) {
        Season season = getByAccess(id);
        season.setStatus(request.status());
        return seasonMapper.toResponse(seasonRepository.save(season));
    }

    @Transactional
    public void delete(UUID id) {
        seasonRepository.delete(getByAccess(id));
    }

    private void validateUnique(UUID tenantId, UUID fieldId, String nome, Integer anoSafra, UUID id) {
        boolean exists = id == null
                ? seasonRepository.existsByTenantIdAndFieldIdAndNomeIgnoreCaseAndAnoSafra(tenantId, fieldId, nome, anoSafra)
                : seasonRepository.existsByTenantIdAndFieldIdAndNomeIgnoreCaseAndAnoSafraAndIdNot(tenantId, fieldId, nome, anoSafra, id);
        if (exists) throw new ConflictException("Season with same field, name and year already exists");
    }

    private void validateDates(java.time.LocalDate dataInicio, java.time.LocalDate dataFim) {
        if (dataFim != null && dataFim.isBefore(dataInicio)) throw new ConflictException("End date cannot be before start date");
    }

    private void validateArea(Field field, java.math.BigDecimal areaPlantada) {
        if (areaPlantada.compareTo(field.getAreaHectares()) > 0) throw new ConflictException("Planted area cannot exceed field area");
    }

    private Field findFieldFromTenant(UUID fieldId, UUID tenantId) {
        Field field = fieldRepository.findById(fieldId).orElseThrow(() -> new NotFoundException("Field not found"));
        if (!field.getTenantId().equals(tenantId)) throw new ConflictException("Field must belong to same tenant");
        return field;
    }

    private Crop findCropFromTenant(UUID cropId, UUID tenantId) {
        Crop crop = cropRepository.findById(cropId).orElseThrow(() -> new NotFoundException("Crop not found"));
        if (!crop.getTenantId().equals(tenantId)) throw new ConflictException("Crop must belong to same tenant");
        return crop;
    }

    private Season getByAccess(UUID id) {
        Season season = seasonRepository.findById(id).orElseThrow(() -> new NotFoundException("Season not found"));
        enforceTenantAccess(getCurrentUser(), season.getTenantId());
        return season;
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

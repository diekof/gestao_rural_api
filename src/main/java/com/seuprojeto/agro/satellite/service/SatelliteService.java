package com.seuprojeto.agro.satellite.service;

import com.seuprojeto.agro.exception.ForbiddenException;
import com.seuprojeto.agro.exception.NotFoundException;
import com.seuprojeto.agro.exception.UnauthorizedException;
import com.seuprojeto.agro.field.domain.Field;
import com.seuprojeto.agro.field.repository.FieldRepository;
import com.seuprojeto.agro.satellite.domain.SatelliteImage;
import com.seuprojeto.agro.satellite.dto.NdviHistoryPointResponse;
import com.seuprojeto.agro.satellite.dto.SatelliteImageCreateRequest;
import com.seuprojeto.agro.satellite.dto.SatelliteImageResponse;
import com.seuprojeto.agro.satellite.repository.SatelliteImageRepository;
import com.seuprojeto.agro.security.AuthenticatedUser;
import com.seuprojeto.agro.security.TenantContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SatelliteService {

    private final SatelliteImageRepository repository;
    private final FieldRepository fieldRepository;

    public SatelliteService(SatelliteImageRepository repository, FieldRepository fieldRepository) {
        this.repository = repository;
        this.fieldRepository = fieldRepository;
    }

    @Transactional
    public SatelliteImageResponse create(SatelliteImageCreateRequest request, UUID tenantIdRequest) {
        UUID tenantId = resolveTenantId(tenantIdRequest);
        validateFieldTenant(request.fieldId(), tenantId);
        SatelliteImage entity = new SatelliteImage();
        entity.setTenantId(tenantId);
        entity.setFieldId(request.fieldId());
        entity.setCapturedAt(request.capturedAt());
        entity.setProvider(request.provider());
        entity.setImageUrl(request.imageUrl());
        entity.setCloudCoverage(request.cloudCoverage());
        entity.setNdviAverage(request.ndviAverage());
        entity.setMetadata(request.metadata());
        return toResponse(repository.save(entity));
    }

    @Transactional(readOnly = true)
    public Page<SatelliteImageResponse> list(UUID fieldId, Pageable pageable) {
        UUID tenantFilter = tenantFilter();
        return repository.findByFilters(tenantFilter, fieldId, pageable).map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public SatelliteImageResponse findById(UUID id) {
        SatelliteImage image = repository.findById(id).orElseThrow(() -> new NotFoundException("Satellite image not found"));
        enforceTenant(image.getTenantId());
        return toResponse(image);
    }

    @Transactional(readOnly = true)
    public List<NdviHistoryPointResponse> ndviHistory(UUID fieldId, LocalDateTime startDate, LocalDateTime endDate) {
        UUID tenantFilter = tenantFilter();
        return repository.findNdviHistory(tenantFilter, fieldId, startDate, endDate).stream()
                .map(s -> new NdviHistoryPointResponse(s.getId(), s.getFieldId(), s.getCapturedAt(), s.getNdviAverage()))
                .toList();
    }

    private SatelliteImageResponse toResponse(SatelliteImage s) {
        return new SatelliteImageResponse(s.getId(), s.getFieldId(), s.getCapturedAt(), s.getProvider(), s.getImageUrl(),
                s.getCloudCoverage(), s.getNdviAverage(), s.getMetadata());
    }

    private void validateFieldTenant(UUID fieldId, UUID tenantId) {
        Field field = fieldRepository.findById(fieldId).orElseThrow(() -> new NotFoundException("Field not found"));
        if (!field.getTenantId().equals(tenantId)) throw new ForbiddenException("Field must belong to same tenant");
    }

    private UUID resolveTenantId(UUID tenantIdRequest) {
        AuthenticatedUser current = getCurrentUser();
        if (current.isSuperAdmin()) {
            if (tenantIdRequest == null) throw new ForbiddenException("tenantId is required for SUPER_ADMIN");
            return tenantIdRequest;
        }
        return TenantContext.getTenantId();
    }

    private UUID tenantFilter() {
        AuthenticatedUser current = getCurrentUser();
        return current.isSuperAdmin() ? null : current.tenantId();
    }

    private void enforceTenant(UUID tenantId) {
        AuthenticatedUser current = getCurrentUser();
        if (!current.isSuperAdmin() && !tenantId.equals(TenantContext.getTenantId())) {
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

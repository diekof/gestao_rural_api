package com.seuprojeto.agro.crop.service;

import com.seuprojeto.agro.common.Status;
import com.seuprojeto.agro.crop.domain.Crop;
import com.seuprojeto.agro.crop.dto.CropCreateRequest;
import com.seuprojeto.agro.crop.dto.CropResponse;
import com.seuprojeto.agro.crop.dto.CropUpdateRequest;
import com.seuprojeto.agro.crop.mapper.CropMapper;
import com.seuprojeto.agro.crop.repository.CropRepository;
import com.seuprojeto.agro.crop.specification.CropSpecification;
import com.seuprojeto.agro.exception.ConflictException;
import com.seuprojeto.agro.exception.ForbiddenException;
import com.seuprojeto.agro.exception.NotFoundException;
import com.seuprojeto.agro.exception.UnauthorizedException;
import com.seuprojeto.agro.security.AuthenticatedUser;
import com.seuprojeto.agro.security.TenantContext;
import com.seuprojeto.agro.season.repository.SeasonRepository;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CropService {

    private final CropRepository cropRepository;
    private final SeasonRepository seasonRepository;
    private final CropMapper cropMapper;

    public CropService(CropRepository cropRepository, SeasonRepository seasonRepository, CropMapper cropMapper) {
        this.cropRepository = cropRepository;
        this.seasonRepository = seasonRepository;
        this.cropMapper = cropMapper;
    }

    @Transactional
    public CropResponse create(CropCreateRequest request, UUID tenantIdRequest) {
        UUID tenantId = resolveTenantId(tenantIdRequest);
        if (cropRepository.existsByTenantIdAndNomeIgnoreCase(tenantId, request.nome())) throw new ConflictException("Crop name already exists");
        Crop crop = cropMapper.toEntity(request);
        crop.setTenantId(tenantId);
        return cropMapper.toResponse(cropRepository.save(crop));
    }

    @Transactional(readOnly = true)
    public CropResponse findById(UUID id) {
        return cropMapper.toResponse(getByAccess(id));
    }

    @Transactional(readOnly = true)
    public Page<CropResponse> list(String nome, String categoria, Status status, Pageable pageable) {
        AuthenticatedUser current = getCurrentUser();
        UUID tenantFilter = current.isSuperAdmin() ? null : current.tenantId();
        return cropRepository.findAll(CropSpecification.filter(tenantFilter, nome, categoria, status), pageable)
                .map(cropMapper::toResponse);
    }

    @Transactional
    public CropResponse update(UUID id, CropUpdateRequest request) {
        Crop crop = getByAccess(id);
        if (cropRepository.existsByTenantIdAndNomeIgnoreCaseAndIdNot(crop.getTenantId(), request.nome(), id)) {
            throw new ConflictException("Crop name already exists");
        }
        cropMapper.updateEntity(crop, request);
        return cropMapper.toResponse(cropRepository.save(crop));
    }

    @Transactional
    public void activate(UUID id) { changeStatus(id, Status.ACTIVE); }

    @Transactional
    public void deactivate(UUID id) { changeStatus(id, Status.INACTIVE); }

    @Transactional
    public void delete(UUID id) {
        Crop crop = getByAccess(id);
        if (seasonRepository.existsByCropId(crop.getId())) {
            throw new ConflictException("Crop has linked seasons and cannot be deleted");
        }
        cropRepository.delete(crop);
    }

    private void changeStatus(UUID id, Status status) {
        Crop crop = getByAccess(id);
        crop.setStatus(status);
        cropRepository.save(crop);
    }

    private Crop getByAccess(UUID id) {
        Crop crop = cropRepository.findById(id).orElseThrow(() -> new NotFoundException("Crop not found"));
        enforceTenantAccess(getCurrentUser(), crop.getTenantId());
        return crop;
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

package com.seuprojeto.agro.farm.service;

import com.seuprojeto.agro.common.Status;
import com.seuprojeto.agro.exception.ConflictException;
import com.seuprojeto.agro.exception.ForbiddenException;
import com.seuprojeto.agro.exception.NotFoundException;
import com.seuprojeto.agro.exception.UnauthorizedException;
import com.seuprojeto.agro.farm.domain.Farm;
import com.seuprojeto.agro.farm.dto.FarmCreateRequest;
import com.seuprojeto.agro.farm.dto.FarmResponse;
import com.seuprojeto.agro.farm.dto.FarmUpdateRequest;
import com.seuprojeto.agro.farm.mapper.FarmMapper;
import com.seuprojeto.agro.farm.repository.FarmRepository;
import com.seuprojeto.agro.farm.specification.FarmSpecification;
import com.seuprojeto.agro.field.repository.FieldRepository;
import com.seuprojeto.agro.security.AuthenticatedUser;
import com.seuprojeto.agro.security.TenantContext;
import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FarmService {

    private final FarmRepository farmRepository;
    private final FieldRepository fieldRepository;
    private final FarmMapper farmMapper;

    public FarmService(FarmRepository farmRepository, FieldRepository fieldRepository, FarmMapper farmMapper) {
        this.farmRepository = farmRepository;
        this.fieldRepository = fieldRepository;
        this.farmMapper = farmMapper;
    }

    @Transactional
    public FarmResponse create(FarmCreateRequest request, UUID tenantIdRequest) {
        UUID tenantId = resolveTenantId(tenantIdRequest);
        if (farmRepository.existsByTenantIdAndNomeIgnoreCase(tenantId, request.nome())) throw new ConflictException("Farm name already exists");
        Farm farm = farmMapper.toEntity(request);
        farm.setTenantId(tenantId);
        return farmMapper.toResponse(farmRepository.save(farm));
    }

    @Transactional(readOnly = true)
    public FarmResponse findById(UUID id) {
        return farmMapper.toResponse(getFarmByAccess(id));
    }

    @Transactional(readOnly = true)
    public Page<FarmResponse> list(String nome, String cidade, String estado, Status status, Pageable pageable) {
        AuthenticatedUser current = getCurrentUser();
        UUID tenantFilter = current.isSuperAdmin() ? null : current.tenantId();
        return farmRepository.findAll(FarmSpecification.filter(tenantFilter, nome, cidade, estado, status), pageable)
                .map(farmMapper::toResponse);
    }

    @Transactional
    public FarmResponse update(UUID id, FarmUpdateRequest request) {
        Farm farm = getFarmByAccess(id);
        if (!farm.getNome().equalsIgnoreCase(request.nome()) && farmRepository.existsByTenantIdAndNomeIgnoreCase(farm.getTenantId(), request.nome())) {
            throw new ConflictException("Farm name already exists");
        }
        BigDecimal occupiedArea = fieldRepository.sumAreaByFarmIdAndStatus(farm.getId(), Status.ACTIVE);
        if (occupiedArea.compareTo(request.areaTotalHectares()) > 0) {
            throw new ConflictException("Farm total area cannot be lower than sum of active fields");
        }
        farmMapper.updateEntity(farm, request);
        return farmMapper.toResponse(farmRepository.save(farm));
    }

    @Transactional
    public void activate(UUID id) { changeStatus(id, Status.ACTIVE); }

    @Transactional
    public void deactivate(UUID id) { changeStatus(id, Status.INACTIVE); }

    @Transactional
    public void delete(UUID id) {
        Farm farm = getFarmByAccess(id);
        if (fieldRepository.countByFarmId(farm.getId()) > 0) throw new ConflictException("Farm has linked fields and cannot be deleted");
        farmRepository.delete(farm);
    }

    private void changeStatus(UUID id, Status status) {
        Farm farm = getFarmByAccess(id);
        farm.setStatus(status);
        farmRepository.save(farm);
    }

    private Farm getFarmByAccess(UUID id) {
        Farm farm = farmRepository.findById(id).orElseThrow(() -> new NotFoundException("Farm not found"));
        enforceTenantAccess(getCurrentUser(), farm.getTenantId());
        return farm;
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

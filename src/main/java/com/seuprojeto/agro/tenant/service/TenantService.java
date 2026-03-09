package com.seuprojeto.agro.tenant.service;

import com.seuprojeto.agro.common.Status;
import com.seuprojeto.agro.exception.ConflictException;
import com.seuprojeto.agro.exception.NotFoundException;
import com.seuprojeto.agro.tenant.domain.Tenant;
import com.seuprojeto.agro.tenant.dto.TenantRequest;
import com.seuprojeto.agro.tenant.dto.TenantResponse;
import com.seuprojeto.agro.tenant.mapper.TenantMapper;
import com.seuprojeto.agro.tenant.repository.TenantRepository;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TenantService {

    private final TenantRepository tenantRepository;
    private final TenantMapper tenantMapper;

    public TenantService(TenantRepository tenantRepository, TenantMapper tenantMapper) {
        this.tenantRepository = tenantRepository;
        this.tenantMapper = tenantMapper;
    }

    @Transactional
    public TenantResponse create(TenantRequest request) {
        if (tenantRepository.existsByCpfCnpj(request.cpfCnpj())) throw new ConflictException("CPF/CNPJ already exists");
        if (tenantRepository.existsByEmail(request.email())) throw new ConflictException("Tenant email already exists");
        Tenant tenant = tenantMapper.toEntity(request);
        return tenantMapper.toResponse(tenantRepository.save(tenant));
    }

    @Transactional(readOnly = true)
    public TenantResponse findById(UUID id) {
        return tenantMapper.toResponse(getEntity(id));
    }

    @Transactional(readOnly = true)
    public Page<TenantResponse> list(Pageable pageable) {
        return tenantRepository.findAll(pageable).map(tenantMapper::toResponse);
    }

    @Transactional
    public TenantResponse update(UUID id, TenantRequest request) {
        Tenant tenant = getEntity(id);
        tenantMapper.updateEntity(tenant, request);
        return tenantMapper.toResponse(tenantRepository.save(tenant));
    }

    @Transactional
    public void activate(UUID id) { setStatus(id, Status.ACTIVE); }

    @Transactional
    public void deactivate(UUID id) { setStatus(id, Status.INACTIVE); }

    private void setStatus(UUID id, Status status) {
        Tenant tenant = getEntity(id);
        tenant.setStatus(status);
        tenantRepository.save(tenant);
    }

    private Tenant getEntity(UUID id) {
        return tenantRepository.findById(id).orElseThrow(() -> new NotFoundException("Tenant not found"));
    }
}

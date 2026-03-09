package com.seuprojeto.agro.machine.service;

import com.seuprojeto.agro.exception.ConflictException;
import com.seuprojeto.agro.exception.ForbiddenException;
import com.seuprojeto.agro.exception.NotFoundException;
import com.seuprojeto.agro.exception.UnauthorizedException;
import com.seuprojeto.agro.machine.domain.Machine;
import com.seuprojeto.agro.machine.domain.MachineStatus;
import com.seuprojeto.agro.machine.domain.MachineType;
import com.seuprojeto.agro.machine.dto.MachineCreateRequest;
import com.seuprojeto.agro.machine.dto.MachineResponse;
import com.seuprojeto.agro.machine.dto.MachineUpdateRequest;
import com.seuprojeto.agro.machine.mapper.MachineMapper;
import com.seuprojeto.agro.machine.repository.MachineRepository;
import com.seuprojeto.agro.machine.specification.MachineSpecification;
import com.seuprojeto.agro.machinerecord.repository.MachineRecordRepository;
import com.seuprojeto.agro.security.AuthenticatedUser;
import com.seuprojeto.agro.security.TenantContext;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MachineService {

    private final MachineRepository repository;
    private final MachineRecordRepository machineRecordRepository;
    private final MachineMapper mapper;

    public MachineService(MachineRepository repository, MachineRecordRepository machineRecordRepository, MachineMapper mapper) {
        this.repository = repository;
        this.machineRecordRepository = machineRecordRepository;
        this.mapper = mapper;
    }

    @Transactional
    public MachineResponse create(MachineCreateRequest request, UUID tenantIdRequest) {
        UUID tenantId = resolveTenantId(tenantIdRequest);
        if (repository.existsByTenantIdAndCodigoIgnoreCase(tenantId, request.codigo())) {
            throw new ConflictException("Machine code already exists");
        }
        Machine machine = mapper.toEntity(request);
        machine.setTenantId(tenantId);
        return mapper.toResponse(repository.save(machine));
    }

    @Transactional(readOnly = true)
    public MachineResponse findById(UUID id) {
        return mapper.toResponse(getByAccess(id));
    }

    @Transactional(readOnly = true)
    public Page<MachineResponse> list(String termo, MachineType tipo, MachineStatus status, Pageable pageable) {
        AuthenticatedUser current = getCurrentUser();
        UUID tenantFilter = current.isSuperAdmin() ? null : current.tenantId();
        return repository.findAll(MachineSpecification.filter(tenantFilter, termo, tipo, status), pageable).map(mapper::toResponse);
    }

    @Transactional
    public MachineResponse update(UUID id, MachineUpdateRequest request) {
        Machine machine = getByAccess(id);
        if (repository.existsByTenantIdAndCodigoIgnoreCaseAndIdNot(machine.getTenantId(), request.codigo(), id)) {
            throw new ConflictException("Machine code already exists");
        }
        mapper.updateEntity(machine, request);
        return mapper.toResponse(repository.save(machine));
    }

    @Transactional
    public void delete(UUID id) {
        Machine machine = getByAccess(id);
        if (machineRecordRepository.existsByMachineId(machine.getId())) {
            throw new ConflictException("Machine has linked records and cannot be deleted");
        }
        repository.delete(machine);
    }

    private Machine getByAccess(UUID id) {
        Machine m = repository.findById(id).orElseThrow(() -> new NotFoundException("Machine not found"));
        enforceTenantAccess(getCurrentUser(), m.getTenantId());
        return m;
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

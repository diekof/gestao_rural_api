package com.seuprojeto.agro.machinerecord.service;

import com.seuprojeto.agro.agriculturaloperation.domain.AgriculturalOperation;
import com.seuprojeto.agro.agriculturaloperation.repository.AgriculturalOperationRepository;
import com.seuprojeto.agro.exception.ConflictException;
import com.seuprojeto.agro.exception.ForbiddenException;
import com.seuprojeto.agro.exception.NotFoundException;
import com.seuprojeto.agro.exception.UnauthorizedException;
import com.seuprojeto.agro.machine.domain.Machine;
import com.seuprojeto.agro.machine.repository.MachineRepository;
import com.seuprojeto.agro.machinerecord.domain.MachineRecord;
import com.seuprojeto.agro.machinerecord.domain.MachineRecordType;
import com.seuprojeto.agro.machinerecord.dto.MachineRecordCreateRequest;
import com.seuprojeto.agro.machinerecord.dto.MachineRecordResponse;
import com.seuprojeto.agro.machinerecord.dto.MachineRecordUpdateRequest;
import com.seuprojeto.agro.machinerecord.mapper.MachineRecordMapper;
import com.seuprojeto.agro.machinerecord.repository.MachineRecordRepository;
import com.seuprojeto.agro.machinerecord.specification.MachineRecordSpecification;
import com.seuprojeto.agro.security.AuthenticatedUser;
import com.seuprojeto.agro.security.TenantContext;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MachineRecordService {

    private final MachineRecordRepository repository;
    private final MachineRepository machineRepository;
    private final AgriculturalOperationRepository operationRepository;
    private final MachineRecordMapper mapper;

    public MachineRecordService(MachineRecordRepository repository, MachineRepository machineRepository,
                                AgriculturalOperationRepository operationRepository, MachineRecordMapper mapper) {
        this.repository = repository;
        this.machineRepository = machineRepository;
        this.operationRepository = operationRepository;
        this.mapper = mapper;
    }

    @Transactional
    public MachineRecordResponse create(MachineRecordCreateRequest request, UUID tenantIdRequest) {
        UUID tenantId = resolveTenantId(tenantIdRequest);
        validateForeignKeys(tenantId, request.machineId(), request.operationId());
        MachineRecord entity = mapper.toEntity(request);
        entity.setTenantId(tenantId);
        MachineRecord saved = repository.save(entity);
        applyMachineHourmeter(request.machineId(), request.tipo(), request.horasTrabalhadas());
        return mapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public MachineRecordResponse findById(UUID id) {
        return mapper.toResponse(getByAccess(id));
    }

    @Transactional(readOnly = true)
    public Page<MachineRecordResponse> list(UUID machineId, UUID operationId, MachineRecordType tipo,
                                            LocalDate dataInicio, LocalDate dataFim, Pageable pageable) {
        AuthenticatedUser current = getCurrentUser();
        UUID tenantFilter = current.isSuperAdmin() ? null : current.tenantId();
        return repository.findAll(MachineRecordSpecification.filter(tenantFilter, machineId, operationId, tipo, dataInicio, dataFim), pageable)
                .map(mapper::toResponse);
    }

    @Transactional
    public MachineRecordResponse update(UUID id, MachineRecordUpdateRequest request) {
        MachineRecord entity = getByAccess(id);
        validateForeignKeys(entity.getTenantId(), request.machineId(), request.operationId());
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public void delete(UUID id) {
        repository.delete(getByAccess(id));
    }

    private void applyMachineHourmeter(UUID machineId, MachineRecordType tipo, BigDecimal horasTrabalhadas) {
        if (tipo != MachineRecordType.USAGE || horasTrabalhadas == null || horasTrabalhadas.compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }
        Machine machine = machineRepository.findById(machineId).orElseThrow(() -> new NotFoundException("Machine not found"));
        machine.setHorimetroAtual(machine.getHorimetroAtual().add(horasTrabalhadas));
        machineRepository.save(machine);
    }

    private void validateForeignKeys(UUID tenantId, UUID machineId, UUID operationId) {
        Machine machine = machineRepository.findById(machineId).orElseThrow(() -> new NotFoundException("Machine not found"));
        if (!machine.getTenantId().equals(tenantId)) throw new ConflictException("Machine must belong to same tenant");
        if (operationId != null) {
            AgriculturalOperation operation = operationRepository.findById(operationId)
                    .orElseThrow(() -> new NotFoundException("Operation not found"));
            if (!operation.getTenantId().equals(tenantId)) throw new ConflictException("Operation must belong to same tenant");
        }
    }

    private MachineRecord getByAccess(UUID id) {
        MachineRecord entity = repository.findById(id).orElseThrow(() -> new NotFoundException("Machine record not found"));
        enforceTenantAccess(getCurrentUser(), entity.getTenantId());
        return entity;
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

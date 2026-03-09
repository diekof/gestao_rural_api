package com.seuprojeto.agro.machinerecord.controller;

import com.seuprojeto.agro.machinerecord.domain.MachineRecordType;
import com.seuprojeto.agro.machinerecord.dto.MachineRecordCreateRequest;
import com.seuprojeto.agro.machinerecord.dto.MachineRecordResponse;
import com.seuprojeto.agro.machinerecord.dto.MachineRecordUpdateRequest;
import com.seuprojeto.agro.machinerecord.service.MachineRecordService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/machine-records")
public class MachineRecordController {

    private final MachineRecordService service;

    public MachineRecordController(MachineRecordService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('TENANT_ADMIN','MANAGER','OPERATOR')")
    public ResponseEntity<MachineRecordResponse> create(@RequestParam(required = false) UUID tenantId,
                                                        @Valid @RequestBody MachineRecordCreateRequest request) {
        return ResponseEntity.ok(service.create(request, tenantId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','TENANT_ADMIN','MANAGER','OPERATOR','VIEWER')")
    public ResponseEntity<MachineRecordResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','TENANT_ADMIN','MANAGER','OPERATOR','VIEWER')")
    public ResponseEntity<Page<MachineRecordResponse>> list(@RequestParam(required = false) UUID machineId,
                                                            @RequestParam(required = false) UUID operationId,
                                                            @RequestParam(required = false) MachineRecordType tipo,
                                                            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
                                                            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
                                                            Pageable pageable) {
        return ResponseEntity.ok(service.list(machineId, operationId, tipo, dataInicio, dataFim, pageable));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('TENANT_ADMIN','MANAGER','OPERATOR')")
    public ResponseEntity<MachineRecordResponse> update(@PathVariable UUID id,
                                                        @Valid @RequestBody MachineRecordUpdateRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('TENANT_ADMIN','MANAGER')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

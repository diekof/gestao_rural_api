package com.seuprojeto.agro.machine.controller;

import com.seuprojeto.agro.machine.domain.MachineStatus;
import com.seuprojeto.agro.machine.domain.MachineType;
import com.seuprojeto.agro.machine.dto.MachineCreateRequest;
import com.seuprojeto.agro.machine.dto.MachineResponse;
import com.seuprojeto.agro.machine.dto.MachineUpdateRequest;
import com.seuprojeto.agro.machine.service.MachineService;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/machines")
public class MachineController {

    private final MachineService service;

    public MachineController(MachineService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','TENANT_ADMIN','MANAGER')")
    public ResponseEntity<MachineResponse> create(@RequestParam(required = false) UUID tenantId,
                                                  @Valid @RequestBody MachineCreateRequest request) {
        return ResponseEntity.ok(service.create(request, tenantId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','TENANT_ADMIN','MANAGER','OPERATOR','VIEWER')")
    public ResponseEntity<MachineResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','TENANT_ADMIN','MANAGER','OPERATOR','VIEWER')")
    public ResponseEntity<Page<MachineResponse>> list(@RequestParam(required = false) String termo,
                                                      @RequestParam(required = false) MachineType tipo,
                                                      @RequestParam(required = false) MachineStatus status,
                                                      Pageable pageable) {
        return ResponseEntity.ok(service.list(termo, tipo, status, pageable));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','TENANT_ADMIN','MANAGER')")
    public ResponseEntity<MachineResponse> update(@PathVariable UUID id, @Valid @RequestBody MachineUpdateRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','TENANT_ADMIN','MANAGER')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

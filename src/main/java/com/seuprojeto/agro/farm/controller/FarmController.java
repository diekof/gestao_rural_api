package com.seuprojeto.agro.farm.controller;

import com.seuprojeto.agro.common.Status;
import com.seuprojeto.agro.farm.dto.FarmCreateRequest;
import com.seuprojeto.agro.farm.dto.FarmResponse;
import com.seuprojeto.agro.farm.dto.FarmUpdateRequest;
import com.seuprojeto.agro.farm.service.FarmService;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/farms")
public class FarmController {

    private final FarmService farmService;

    public FarmController(FarmService farmService) {
        this.farmService = farmService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('TENANT_ADMIN','MANAGER')")
    public ResponseEntity<FarmResponse> create(@RequestParam(required = false) UUID tenantId, @Valid @RequestBody FarmCreateRequest request) {
        return ResponseEntity.ok(farmService.create(request, tenantId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','TENANT_ADMIN','MANAGER','OPERATOR','VIEWER')")
    public ResponseEntity<FarmResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(farmService.findById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','TENANT_ADMIN','MANAGER','OPERATOR','VIEWER')")
    public ResponseEntity<Page<FarmResponse>> list(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String cidade,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) Status status,
            Pageable pageable) {
        return ResponseEntity.ok(farmService.list(nome, cidade, estado, status, pageable));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('TENANT_ADMIN','MANAGER')")
    public ResponseEntity<FarmResponse> update(@PathVariable UUID id, @Valid @RequestBody FarmUpdateRequest request) {
        return ResponseEntity.ok(farmService.update(id, request));
    }

    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasAnyRole('TENANT_ADMIN','MANAGER')")
    public ResponseEntity<Void> activate(@PathVariable UUID id) {
        farmService.activate(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('TENANT_ADMIN','MANAGER')")
    public ResponseEntity<Void> deactivate(@PathVariable UUID id) {
        farmService.deactivate(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('TENANT_ADMIN','MANAGER')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        farmService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

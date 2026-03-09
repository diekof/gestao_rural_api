package com.seuprojeto.agro.crop.controller;

import com.seuprojeto.agro.common.Status;
import com.seuprojeto.agro.crop.dto.CropCreateRequest;
import com.seuprojeto.agro.crop.dto.CropResponse;
import com.seuprojeto.agro.crop.dto.CropUpdateRequest;
import com.seuprojeto.agro.crop.service.CropService;
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
@RequestMapping("/api/crops")
public class CropController {

    private final CropService cropService;

    public CropController(CropService cropService) {
        this.cropService = cropService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('TENANT_ADMIN','MANAGER')")
    public ResponseEntity<CropResponse> create(@RequestParam(required = false) UUID tenantId, @Valid @RequestBody CropCreateRequest request) {
        return ResponseEntity.ok(cropService.create(request, tenantId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','TENANT_ADMIN','MANAGER','OPERATOR','VIEWER')")
    public ResponseEntity<CropResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(cropService.findById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','TENANT_ADMIN','MANAGER','OPERATOR','VIEWER')")
    public ResponseEntity<Page<CropResponse>> list(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) Status status,
            Pageable pageable) {
        return ResponseEntity.ok(cropService.list(nome, categoria, status, pageable));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('TENANT_ADMIN','MANAGER')")
    public ResponseEntity<CropResponse> update(@PathVariable UUID id, @Valid @RequestBody CropUpdateRequest request) {
        return ResponseEntity.ok(cropService.update(id, request));
    }

    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasAnyRole('TENANT_ADMIN','MANAGER')")
    public ResponseEntity<Void> activate(@PathVariable UUID id) {
        cropService.activate(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('TENANT_ADMIN','MANAGER')")
    public ResponseEntity<Void> deactivate(@PathVariable UUID id) {
        cropService.deactivate(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('TENANT_ADMIN','MANAGER')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        cropService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

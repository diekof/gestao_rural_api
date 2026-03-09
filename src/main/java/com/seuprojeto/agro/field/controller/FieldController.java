package com.seuprojeto.agro.field.controller;

import com.seuprojeto.agro.common.Status;
import com.seuprojeto.agro.field.dto.FieldCreateRequest;
import com.seuprojeto.agro.field.dto.FieldResponse;
import com.seuprojeto.agro.field.dto.FieldUpdateRequest;
import com.seuprojeto.agro.field.service.FieldService;
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
@RequestMapping("/api/fields")
public class FieldController {

    private final FieldService fieldService;

    public FieldController(FieldService fieldService) {
        this.fieldService = fieldService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('TENANT_ADMIN','MANAGER')")
    public ResponseEntity<FieldResponse> create(@RequestParam(required = false) UUID tenantId, @Valid @RequestBody FieldCreateRequest request) {
        return ResponseEntity.ok(fieldService.create(request, tenantId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','TENANT_ADMIN','MANAGER','OPERATOR','VIEWER')")
    public ResponseEntity<FieldResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(fieldService.findById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','TENANT_ADMIN','MANAGER','OPERATOR','VIEWER')")
    public ResponseEntity<Page<FieldResponse>> list(
            @RequestParam(required = false) UUID farmId,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) String tipoSolo,
            Pageable pageable) {
        return ResponseEntity.ok(fieldService.list(farmId, nome, status, tipoSolo, pageable));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('TENANT_ADMIN','MANAGER')")
    public ResponseEntity<FieldResponse> update(@PathVariable UUID id, @Valid @RequestBody FieldUpdateRequest request) {
        return ResponseEntity.ok(fieldService.update(id, request));
    }

    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasAnyRole('TENANT_ADMIN','MANAGER')")
    public ResponseEntity<Void> activate(@PathVariable UUID id) {
        fieldService.activate(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('TENANT_ADMIN','MANAGER')")
    public ResponseEntity<Void> deactivate(@PathVariable UUID id) {
        fieldService.deactivate(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('TENANT_ADMIN','MANAGER')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        fieldService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

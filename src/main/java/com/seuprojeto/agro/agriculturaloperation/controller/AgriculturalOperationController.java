package com.seuprojeto.agro.agriculturaloperation.controller;

import com.seuprojeto.agro.agriculturaloperation.domain.OperationStatus;
import com.seuprojeto.agro.agriculturaloperation.domain.OperationType;
import com.seuprojeto.agro.agriculturaloperation.dto.AgriculturalOperationCreateRequest;
import com.seuprojeto.agro.agriculturaloperation.dto.AgriculturalOperationResponse;
import com.seuprojeto.agro.agriculturaloperation.dto.AgriculturalOperationUpdateRequest;
import com.seuprojeto.agro.agriculturaloperation.service.AgriculturalOperationService;
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
@RequestMapping("/api/agricultural-operations")
public class AgriculturalOperationController {

    private final AgriculturalOperationService service;

    public AgriculturalOperationController(AgriculturalOperationService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('TENANT_ADMIN','MANAGER','OPERATOR')")
    public ResponseEntity<AgriculturalOperationResponse> create(@RequestParam(required = false) UUID tenantId,
                                                                @Valid @RequestBody AgriculturalOperationCreateRequest request) {
        return ResponseEntity.ok(service.create(request, tenantId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','TENANT_ADMIN','MANAGER','OPERATOR','VIEWER')")
    public ResponseEntity<AgriculturalOperationResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','TENANT_ADMIN','MANAGER','OPERATOR','VIEWER')")
    public ResponseEntity<Page<AgriculturalOperationResponse>> list(@RequestParam(required = false) UUID seasonId,
                                                                    @RequestParam(required = false) UUID fieldId,
                                                                    @RequestParam(required = false) OperationType tipo,
                                                                    @RequestParam(required = false) OperationStatus status,
                                                                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
                                                                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
                                                                    Pageable pageable) {
        return ResponseEntity.ok(service.list(seasonId, fieldId, tipo, status, dataInicio, dataFim, pageable));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('TENANT_ADMIN','MANAGER','OPERATOR')")
    public ResponseEntity<AgriculturalOperationResponse> update(@PathVariable UUID id,
                                                                @Valid @RequestBody AgriculturalOperationUpdateRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('TENANT_ADMIN','MANAGER')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

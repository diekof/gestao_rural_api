package com.seuprojeto.agro.fuelsupply.controller;

import com.seuprojeto.agro.fuelsupply.dto.FuelSupplyCreateRequest;
import com.seuprojeto.agro.fuelsupply.dto.FuelSupplyResponse;
import com.seuprojeto.agro.fuelsupply.dto.FuelSupplyUpdateRequest;
import com.seuprojeto.agro.fuelsupply.service.FuelSupplyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fuel-supplies")
public class FuelSupplyController {

    private final FuelSupplyService service;

    public FuelSupplyController(FuelSupplyService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','TENANT_ADMIN','MANAGER','OPERATOR')")
    public ResponseEntity<FuelSupplyResponse> create(
            @RequestParam(required = false) UUID tenantId,
            @Valid @RequestBody FuelSupplyCreateRequest request) {
        return ResponseEntity.ok(service.create(request, tenantId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','TENANT_ADMIN','MANAGER','OPERATOR')")
    public ResponseEntity<FuelSupplyResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','TENANT_ADMIN','MANAGER','OPERATOR')")
    public ResponseEntity<Page<FuelSupplyResponse>> list(
            @RequestParam(required = false) UUID userId,
            @RequestParam(required = false) UUID machineId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime dataInicio,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime dataFim,
            Pageable pageable) {
        return ResponseEntity.ok(service.list(userId, machineId, dataInicio, dataFim, pageable));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','TENANT_ADMIN','MANAGER')")
    public ResponseEntity<FuelSupplyResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody FuelSupplyUpdateRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','TENANT_ADMIN','MANAGER')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

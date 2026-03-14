package com.seuprojeto.agro.fuelcredit.controller;

import com.seuprojeto.agro.fuelcredit.domain.FuelCreditStatus;
import com.seuprojeto.agro.fuelcredit.dto.FuelCreditCreateRequest;
import com.seuprojeto.agro.fuelcredit.dto.FuelCreditResponse;
import com.seuprojeto.agro.fuelcredit.dto.FuelCreditUpdateRequest;
import com.seuprojeto.agro.fuelcredit.service.FuelCreditService;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fuel-credits")
public class FuelCreditController {

    private final FuelCreditService service;

    public FuelCreditController(FuelCreditService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','TENANT_ADMIN','MANAGER')")
    public ResponseEntity<FuelCreditResponse> create(@RequestParam(required = false) UUID tenantId,
                                                     @Valid @RequestBody FuelCreditCreateRequest request) {
        return ResponseEntity.ok(service.create(request, tenantId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','TENANT_ADMIN','MANAGER','OPERATOR')")
    public ResponseEntity<FuelCreditResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','TENANT_ADMIN','MANAGER','OPERATOR')")
    public ResponseEntity<Page<FuelCreditResponse>> list(@RequestParam(required = false) UUID userId,
                                                         @RequestParam(required = false) FuelCreditStatus status,
                                                         Pageable pageable) {
        return ResponseEntity.ok(service.list(userId, status, pageable));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','TENANT_ADMIN','MANAGER')")
    public ResponseEntity<FuelCreditResponse> update(@PathVariable UUID id,
                                                     @Valid @RequestBody FuelCreditUpdateRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }
}


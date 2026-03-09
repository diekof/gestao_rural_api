package com.seuprojeto.agro.financialentry.controller;

import com.seuprojeto.agro.financialentry.domain.FinancialCategory;
import com.seuprojeto.agro.financialentry.domain.FinancialEntryStatus;
import com.seuprojeto.agro.financialentry.domain.FinancialEntryType;
import com.seuprojeto.agro.financialentry.dto.FinancialEntryCreateRequest;
import com.seuprojeto.agro.financialentry.dto.FinancialEntryResponse;
import com.seuprojeto.agro.financialentry.dto.FinancialEntryUpdateRequest;
import com.seuprojeto.agro.financialentry.service.FinancialEntryService;
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
@RequestMapping("/api/financial-entries")
public class FinancialEntryController {

    private final FinancialEntryService service;

    public FinancialEntryController(FinancialEntryService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('TENANT_ADMIN','MANAGER')")
    public ResponseEntity<FinancialEntryResponse> create(@RequestParam(required = false) UUID tenantId,
                                                         @Valid @RequestBody FinancialEntryCreateRequest request) {
        return ResponseEntity.ok(service.create(request, tenantId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','TENANT_ADMIN','MANAGER','OPERATOR','VIEWER')")
    public ResponseEntity<FinancialEntryResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','TENANT_ADMIN','MANAGER','OPERATOR','VIEWER')")
    public ResponseEntity<Page<FinancialEntryResponse>> list(@RequestParam(required = false) UUID seasonId,
                                                             @RequestParam(required = false) UUID operationId,
                                                             @RequestParam(required = false) FinancialEntryType tipo,
                                                             @RequestParam(required = false) FinancialCategory categoria,
                                                             @RequestParam(required = false) FinancialEntryStatus status,
                                                             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
                                                             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
                                                             Pageable pageable) {
        return ResponseEntity.ok(service.list(seasonId, operationId, tipo, categoria, status, dataInicio, dataFim, pageable));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('TENANT_ADMIN','MANAGER')")
    public ResponseEntity<FinancialEntryResponse> update(@PathVariable UUID id,
                                                         @Valid @RequestBody FinancialEntryUpdateRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('TENANT_ADMIN','MANAGER')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

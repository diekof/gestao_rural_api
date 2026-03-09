package com.seuprojeto.agro.season.controller;

import com.seuprojeto.agro.season.domain.SeasonStatus;
import com.seuprojeto.agro.season.dto.SeasonCreateRequest;
import com.seuprojeto.agro.season.dto.SeasonResponse;
import com.seuprojeto.agro.season.dto.SeasonStatusUpdateRequest;
import com.seuprojeto.agro.season.dto.SeasonUpdateRequest;
import com.seuprojeto.agro.season.service.SeasonService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
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
@RequestMapping("/api/seasons")
public class SeasonController {

    private final SeasonService seasonService;

    public SeasonController(SeasonService seasonService) {
        this.seasonService = seasonService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('TENANT_ADMIN','MANAGER')")
    public ResponseEntity<SeasonResponse> create(@RequestParam(required = false) UUID tenantId, @Valid @RequestBody SeasonCreateRequest request) {
        return ResponseEntity.ok(seasonService.create(request, tenantId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','TENANT_ADMIN','MANAGER','OPERATOR','VIEWER')")
    public ResponseEntity<SeasonResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(seasonService.findById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','TENANT_ADMIN','MANAGER','OPERATOR','VIEWER')")
    public ResponseEntity<Page<SeasonResponse>> list(
            @RequestParam(required = false) UUID fieldId,
            @RequestParam(required = false) UUID cropId,
            @RequestParam(required = false) Integer anoSafra,
            @RequestParam(required = false) SeasonStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicioFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicioTo,
            Pageable pageable) {
        return ResponseEntity.ok(seasonService.list(fieldId, cropId, anoSafra, status, dataInicioFrom, dataInicioTo, pageable));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('TENANT_ADMIN','MANAGER')")
    public ResponseEntity<SeasonResponse> update(@PathVariable UUID id, @Valid @RequestBody SeasonUpdateRequest request) {
        return ResponseEntity.ok(seasonService.update(id, request));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('TENANT_ADMIN','MANAGER')")
    public ResponseEntity<SeasonResponse> updateStatus(@PathVariable UUID id, @Valid @RequestBody SeasonStatusUpdateRequest request) {
        return ResponseEntity.ok(seasonService.updateStatus(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('TENANT_ADMIN','MANAGER')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        seasonService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

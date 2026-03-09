package com.seuprojeto.agro.satellite.controller;

import com.seuprojeto.agro.satellite.dto.NdviHistoryPointResponse;
import com.seuprojeto.agro.satellite.dto.SatelliteImageCreateRequest;
import com.seuprojeto.agro.satellite.dto.SatelliteImageResponse;
import com.seuprojeto.agro.satellite.service.SatelliteService;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/satellite")
public class SatelliteController {

    private final SatelliteService service;

    public SatelliteController(SatelliteService service) {
        this.service = service;
    }

    @PostMapping("/images")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','TENANT_ADMIN','MANAGER','OPERATOR')")
    public ResponseEntity<SatelliteImageResponse> create(@RequestParam(required = false) UUID tenantId,
                                                         @Valid @RequestBody SatelliteImageCreateRequest request) {
        return ResponseEntity.ok(service.create(request, tenantId));
    }

    @GetMapping("/images")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','TENANT_ADMIN','MANAGER','OPERATOR','VIEWER')")
    public ResponseEntity<Page<SatelliteImageResponse>> list(@RequestParam(required = false) UUID fieldId,
                                                             Pageable pageable) {
        return ResponseEntity.ok(service.list(fieldId, pageable));
    }

    @GetMapping("/images/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','TENANT_ADMIN','MANAGER','OPERATOR','VIEWER')")
    public ResponseEntity<SatelliteImageResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/ndvi-history")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','TENANT_ADMIN','MANAGER','OPERATOR','VIEWER')")
    public ResponseEntity<List<NdviHistoryPointResponse>> ndviHistory(@RequestParam(required = false) UUID fieldId,
                                                                      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                                                                      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(service.ndviHistory(fieldId, startDate, endDate));
    }
}

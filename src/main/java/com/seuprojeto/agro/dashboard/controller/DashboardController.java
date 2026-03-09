package com.seuprojeto.agro.dashboard.controller;

import com.seuprojeto.agro.dashboard.dto.FinancialSummaryResponse;
import com.seuprojeto.agro.dashboard.dto.OperationsSummaryResponse;
import com.seuprojeto.agro.dashboard.dto.OverviewResponse;
import com.seuprojeto.agro.dashboard.dto.ProductionSummaryResponse;
import com.seuprojeto.agro.dashboard.service.DashboardService;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService service;

    public DashboardController(DashboardService service) {
        this.service = service;
    }

    @GetMapping("/overview")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','TENANT_ADMIN','MANAGER','VIEWER')")
    public ResponseEntity<OverviewResponse> overview(@RequestParam(required = false) UUID tenantId) {
        return ResponseEntity.ok(service.overview(tenantId));
    }

    @GetMapping("/financial-summary")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','TENANT_ADMIN','MANAGER','VIEWER')")
    public ResponseEntity<FinancialSummaryResponse> financialSummary(@RequestParam(required = false) UUID tenantId) {
        return ResponseEntity.ok(service.financialSummary(tenantId));
    }

    @GetMapping("/production-summary")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','TENANT_ADMIN','MANAGER','VIEWER')")
    public ResponseEntity<ProductionSummaryResponse> productionSummary(@RequestParam(required = false) UUID tenantId) {
        return ResponseEntity.ok(service.productionSummary(tenantId));
    }

    @GetMapping("/operations-summary")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','TENANT_ADMIN','MANAGER','VIEWER')")
    public ResponseEntity<OperationsSummaryResponse> operationsSummary(@RequestParam(required = false) UUID tenantId) {
        return ResponseEntity.ok(service.operationsSummary(tenantId));
    }
}

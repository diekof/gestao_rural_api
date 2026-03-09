package com.seuprojeto.agro.ai.controller;

import com.seuprojeto.agro.ai.dto.ProductivityForecastRequest;
import com.seuprojeto.agro.ai.dto.ProductivityForecastResponse;
import com.seuprojeto.agro.ai.dto.RiskAnalysisRequest;
import com.seuprojeto.agro.ai.dto.RiskAnalysisResponse;
import com.seuprojeto.agro.ai.service.AIService;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AIController {

    private final AIService service;

    public AIController(AIService service) {
        this.service = service;
    }

    @PostMapping("/productivity-forecast")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','TENANT_ADMIN','MANAGER')")
    public ResponseEntity<ProductivityForecastResponse> productivityForecast(@RequestParam(required = false) UUID tenantId,
                                                                             @Valid @RequestBody ProductivityForecastRequest request) {
        return ResponseEntity.ok(service.productivityForecast(request, tenantId));
    }

    @PostMapping("/risk-analysis")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','TENANT_ADMIN','MANAGER')")
    public ResponseEntity<RiskAnalysisResponse> riskAnalysis(@RequestParam(required = false) UUID tenantId,
                                                             @Valid @RequestBody RiskAnalysisRequest request) {
        return ResponseEntity.ok(service.riskAnalysis(request, tenantId));
    }
}

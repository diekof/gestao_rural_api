package com.seuprojeto.agro.ai.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seuprojeto.agro.ai.domain.AIAnalysis;
import com.seuprojeto.agro.ai.domain.AIAnalysisType;
import com.seuprojeto.agro.ai.dto.ProductivityForecastRequest;
import com.seuprojeto.agro.ai.dto.ProductivityForecastResponse;
import com.seuprojeto.agro.ai.dto.RiskAnalysisRequest;
import com.seuprojeto.agro.ai.dto.RiskAnalysisResponse;
import com.seuprojeto.agro.ai.repository.AIAnalysisRepository;
import com.seuprojeto.agro.exception.ForbiddenException;
import com.seuprojeto.agro.exception.UnauthorizedException;
import com.seuprojeto.agro.security.AuthenticatedUser;
import com.seuprojeto.agro.security.TenantContext;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.UUID;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AIService {

    private final AIAnalysisRepository repository;
    private final ObjectMapper objectMapper;

    public AIService(AIAnalysisRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public ProductivityForecastResponse productivityForecast(ProductivityForecastRequest request, UUID tenantIdRequest) {
        UUID tenantId = resolveTenantId(tenantIdRequest);
        BigDecimal investmentPerHa = request.investmentAmount().divide(request.areaHectares(), 4, RoundingMode.HALF_UP);

        BigDecimal base = request.ndvi().multiply(new BigDecimal("35"))
                .add(request.expectedRainfallMm().min(new BigDecimal("300")).divide(new BigDecimal("10"), 4, RoundingMode.HALF_UP))
                .add(investmentPerHa.min(new BigDecimal("3500")).divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP));
        BigDecimal forecast = base.max(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);

        BigDecimal confidence = request.ndvi().multiply(new BigDecimal("70"))
                .add(new BigDecimal("20"))
                .min(new BigDecimal("99"))
                .setScale(2, RoundingMode.HALF_UP);

        String resumo = "Previsão heurística de produtividade calculada com NDVI, chuva e investimento por hectare.";
        String recomendacoes = forecast.compareTo(new BigDecimal("70")) >= 0
                ? "Manter estratégia atual e monitorar janelas operacionais."
                : "Reavaliar manejo nutricional e calendarização de operações para elevar produtividade.";

        AIAnalysis saved = persistAnalysis(tenantId, AIAnalysisType.PRODUCTIVITY_FORECAST, request.seasonId(), request.fieldId(),
                confidence, resumo, recomendacoes,
                Map.of("areaHectares", request.areaHectares(), "expectedRainfallMm", request.expectedRainfallMm(), "ndvi", request.ndvi(), "investmentAmount", request.investmentAmount()),
                Map.of("forecastedProductivity", forecast, "confidenceScore", confidence));

        return new ProductivityForecastResponse(saved.getId(), forecast, confidence, resumo, recomendacoes, saved.getAnalysisDate());
    }

    @Transactional
    public RiskAnalysisResponse riskAnalysis(RiskAnalysisRequest request, UUID tenantIdRequest) {
        UUID tenantId = resolveTenantId(tenantIdRequest);
        BigDecimal riskScore = request.climateRisk().multiply(new BigDecimal("0.35"))
                .add(request.pestPressure().multiply(new BigDecimal("0.30")))
                .add(request.soilMoistureStress().multiply(new BigDecimal("0.20")))
                .add(request.cloudCoverageRisk().multiply(new BigDecimal("0.15")))
                .multiply(new BigDecimal("100"))
                .setScale(2, RoundingMode.HALF_UP);

        String level = riskScore.compareTo(new BigDecimal("70")) >= 0 ? "HIGH"
                : riskScore.compareTo(new BigDecimal("40")) >= 0 ? "MEDIUM" : "LOW";

        String resumo = "Análise heurística composta por risco climático, pressão de pragas e estresse hídrico.";
        String recomendacoes = switch (level) {
            case "HIGH" -> "Aumentar frequência de monitoramento, revisar plano fitossanitário e contingência climática.";
            case "MEDIUM" -> "Monitoramento quinzenal e ajustes preventivos no manejo.";
            default -> "Manter rotina atual de monitoramento.";
        };

        AIAnalysis saved = persistAnalysis(tenantId, AIAnalysisType.RISK_ANALYSIS, request.seasonId(), request.fieldId(),
                riskScore, resumo, recomendacoes,
                Map.of("climateRisk", request.climateRisk(), "pestPressure", request.pestPressure(), "soilMoistureStress", request.soilMoistureStress(), "cloudCoverageRisk", request.cloudCoverageRisk()),
                Map.of("riskScore", riskScore, "riskLevel", level));

        return new RiskAnalysisResponse(saved.getId(), riskScore, level, resumo, recomendacoes, saved.getAnalysisDate());
    }

    private AIAnalysis persistAnalysis(UUID tenantId, AIAnalysisType type, UUID seasonId, UUID fieldId, BigDecimal score,
                                       String resumo, String recomendacoes, Map<String, Object> input, Map<String, Object> output) {
        AIAnalysis entity = new AIAnalysis();
        entity.setTenantId(tenantId);
        entity.setTipo(type);
        entity.setSeasonId(seasonId);
        entity.setFieldId(fieldId);
        entity.setScore(score);
        entity.setResumo(resumo);
        entity.setRecomendacoes(recomendacoes);
        entity.setInputData(toJson(input));
        entity.setResultData(toJson(output));
        return repository.save(entity);
    }

    private String toJson(Object payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }

    private UUID resolveTenantId(UUID tenantIdRequest) {
        AuthenticatedUser current = getCurrentUser();
        if (current.isSuperAdmin()) {
            if (tenantIdRequest == null) throw new ForbiddenException("tenantId is required for SUPER_ADMIN");
            return tenantIdRequest;
        }
        return TenantContext.getTenantId();
    }

    private AuthenticatedUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof AuthenticatedUser authUser)) {
            throw new UnauthorizedException("Unauthenticated");
        }
        return authUser;
    }
}

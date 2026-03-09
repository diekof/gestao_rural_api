package com.seuprojeto.agro.ai.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record RiskAnalysisResponse(
        UUID analysisId,
        BigDecimal riskScore,
        String riskLevel,
        String resumo,
        String recomendacoes,
        LocalDateTime analysisDate
) {
}

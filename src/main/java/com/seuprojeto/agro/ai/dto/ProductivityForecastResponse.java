package com.seuprojeto.agro.ai.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ProductivityForecastResponse(
        UUID analysisId,
        BigDecimal forecastedProductivity,
        BigDecimal confidenceScore,
        String resumo,
        String recomendacoes,
        LocalDateTime analysisDate
) {
}

package com.seuprojeto.agro.ai.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

public record RiskAnalysisRequest(
        UUID seasonId,
        UUID fieldId,
        @NotNull @DecimalMin("0.0") @DecimalMax("1.0") BigDecimal climateRisk,
        @NotNull @DecimalMin("0.0") @DecimalMax("1.0") BigDecimal pestPressure,
        @NotNull @DecimalMin("0.0") @DecimalMax("1.0") BigDecimal soilMoistureStress,
        @NotNull @DecimalMin("0.0") @DecimalMax("1.0") BigDecimal cloudCoverageRisk
) {
}

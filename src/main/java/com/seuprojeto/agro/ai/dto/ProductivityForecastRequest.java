package com.seuprojeto.agro.ai.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

public record ProductivityForecastRequest(
        UUID seasonId,
        UUID fieldId,
        @NotNull @DecimalMin("0.1") BigDecimal areaHectares,
        @NotNull @DecimalMin("0") BigDecimal expectedRainfallMm,
        @NotNull @DecimalMin("0.0") @DecimalMax("1.0") BigDecimal ndvi,
        @NotNull @DecimalMin("0") BigDecimal investmentAmount
) {
}

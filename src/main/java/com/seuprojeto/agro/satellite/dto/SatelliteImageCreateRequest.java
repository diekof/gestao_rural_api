package com.seuprojeto.agro.satellite.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record SatelliteImageCreateRequest(
        @NotNull UUID fieldId,
        @NotNull LocalDateTime capturedAt,
        @NotBlank String provider,
        @NotBlank String imageUrl,
        @DecimalMin("0") @DecimalMax("100") BigDecimal cloudCoverage,
        @DecimalMin("-1") @DecimalMax("1") BigDecimal ndviAverage,
        String metadata
) {
}

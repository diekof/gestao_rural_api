package com.seuprojeto.agro.satellite.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record SatelliteImageResponse(
        UUID id,
        UUID fieldId,
        LocalDateTime capturedAt,
        String provider,
        String imageUrl,
        BigDecimal cloudCoverage,
        BigDecimal ndviAverage,
        String metadata
) {
}

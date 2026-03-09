package com.seuprojeto.agro.satellite.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record NdviHistoryPointResponse(
        UUID imageId,
        UUID fieldId,
        LocalDateTime capturedAt,
        BigDecimal ndviAverage
) {
}

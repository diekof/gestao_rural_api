package com.seuprojeto.agro.dashboard.dto;

import java.math.BigDecimal;
import java.util.List;

public record OperationsSummaryResponse(
        long totalOperations,
        long planned,
        long inProgress,
        long completed,
        long canceled,
        BigDecimal totalAreaExecuted,
        BigDecimal totalEstimatedCost,
        BigDecimal totalRealCost,
        List<OperationTypeSummaryResponse> byType
) {
    public record OperationTypeSummaryResponse(String tipo, long total, BigDecimal area, BigDecimal cost) {}
}

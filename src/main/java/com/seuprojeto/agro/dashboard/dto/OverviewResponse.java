package com.seuprojeto.agro.dashboard.dto;

import java.math.BigDecimal;

public record OverviewResponse(
        long totalFarms,
        long totalFields,
        long activeSeasons,
        long operationsCurrentMonth,
        BigDecimal totalRevenue,
        BigDecimal totalExpense,
        BigDecimal netBalance
) {
}

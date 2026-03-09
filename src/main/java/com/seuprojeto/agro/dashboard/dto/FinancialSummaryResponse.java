package com.seuprojeto.agro.dashboard.dto;

import java.math.BigDecimal;
import java.util.List;

public record FinancialSummaryResponse(
        BigDecimal totalRevenue,
        BigDecimal totalExpense,
        BigDecimal totalPending,
        BigDecimal totalPaid,
        BigDecimal netBalance,
        List<CategoryAmountResponse> byCategory
) {
    public record CategoryAmountResponse(String categoria, BigDecimal total) {}
}

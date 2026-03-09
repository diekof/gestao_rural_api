package com.seuprojeto.agro.dashboard.dto;

import java.math.BigDecimal;
import java.util.List;

public record ProductionSummaryResponse(
        BigDecimal totalPlantedArea,
        BigDecimal totalExpectedProduction,
        BigDecimal totalActualProduction,
        BigDecimal avgExpectedYield,
        BigDecimal avgActualYield,
        List<CropProductionResponse> byCrop
) {
    public record CropProductionResponse(String cropName, BigDecimal plantedArea, BigDecimal expectedProduction, BigDecimal actualProduction) {}
}

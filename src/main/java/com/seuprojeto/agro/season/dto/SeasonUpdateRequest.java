package com.seuprojeto.agro.season.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record SeasonUpdateRequest(
        @NotNull UUID fieldId,
        @NotNull UUID cropId,
        @NotBlank String nome,
        @NotNull @Positive Integer anoSafra,
        @NotNull LocalDate dataInicio,
        LocalDate dataFim,
        @NotNull @DecimalMin(value = "0.01") BigDecimal areaPlantada,
        @DecimalMin(value = "0.01") BigDecimal previsaoProducao,
        @DecimalMin(value = "0.01") BigDecimal producaoReal,
        String observacoes
) {}

package com.seuprojeto.agro.season.dto;

import com.seuprojeto.agro.season.domain.SeasonStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public record SeasonResponse(
        UUID id,
        UUID tenantId,
        UUID fieldId,
        UUID cropId,
        String nome,
        Integer anoSafra,
        LocalDate dataInicio,
        LocalDate dataFim,
        BigDecimal areaPlantada,
        SeasonStatus status,
        BigDecimal previsaoProducao,
        BigDecimal producaoReal,
        String observacoes,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        String createdBy,
        String updatedBy
) {}

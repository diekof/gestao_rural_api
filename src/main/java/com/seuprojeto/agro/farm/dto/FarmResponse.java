package com.seuprojeto.agro.farm.dto;

import com.seuprojeto.agro.common.Status;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record FarmResponse(
        UUID id,
        UUID tenantId,
        String nome,
        String proprietario,
        BigDecimal areaTotalHectares,
        String cidade,
        String estado,
        BigDecimal latitude,
        BigDecimal longitude,
        String observacoes,
        Status status,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        String createdBy,
        String updatedBy
) {}

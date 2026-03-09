package com.seuprojeto.agro.crop.dto;

import com.seuprojeto.agro.common.Status;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record CropResponse(
        UUID id,
        UUID tenantId,
        String nome,
        String categoria,
        Integer cicloMedioDias,
        BigDecimal produtividadeEsperada,
        String unidadeProdutividade,
        String descricao,
        Status status,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        String createdBy,
        String updatedBy
) {}

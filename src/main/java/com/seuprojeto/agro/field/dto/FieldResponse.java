package com.seuprojeto.agro.field.dto;

import com.seuprojeto.agro.common.Status;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record FieldResponse(
        UUID id,
        UUID tenantId,
        UUID farmId,
        String nome,
        String codigo,
        BigDecimal areaHectares,
        String tipoSolo,
        Status status,
        String geoJson,
        String observacoes,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        String createdBy,
        String updatedBy
) {}

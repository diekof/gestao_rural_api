package com.seuprojeto.agro.agriculturaloperation.dto;

import com.seuprojeto.agro.agriculturaloperation.domain.OperationStatus;
import com.seuprojeto.agro.agriculturaloperation.domain.OperationType;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record AgriculturalOperationResponse(
        UUID id,
        UUID tenantId,
        UUID seasonId,
        UUID fieldId,
        OperationType tipo,
        OperationStatus status,
        LocalDate dataOperacao,
        BigDecimal areaExecutada,
        BigDecimal custoEstimado,
        BigDecimal custoReal,
        String descricao,
        Instant createdAt,
        Instant updatedAt,
        String createdBy,
        String updatedBy
) {}

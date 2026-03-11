package com.seuprojeto.agro.fuelsupply.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record FuelSupplyResponse(
        UUID id,
        UUID tenantId,
        UUID creditId,
        UUID userId,
        String workerName,
        UUID machineId,
        String machineName,
        BigDecimal litros,
        BigDecimal valor,
        Instant abastecidoEm,
        String localizacao,
        String observacao,
        Instant createdAt,
        Instant updatedAt,
        String createdBy,
        String updatedBy
) {}


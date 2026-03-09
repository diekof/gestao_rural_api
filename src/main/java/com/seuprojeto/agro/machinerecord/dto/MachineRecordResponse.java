package com.seuprojeto.agro.machinerecord.dto;

import com.seuprojeto.agro.machinerecord.domain.MachineRecordType;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record MachineRecordResponse(
        UUID id,
        UUID tenantId,
        UUID machineId,
        UUID operationId,
        MachineRecordType tipo,
        LocalDate dataRegistro,
        BigDecimal horasTrabalhadas,
        BigDecimal valor,
        String descricao,
        Instant createdAt,
        Instant updatedAt,
        String createdBy,
        String updatedBy
) {}

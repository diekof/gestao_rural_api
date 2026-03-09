package com.seuprojeto.agro.machine.dto;

import com.seuprojeto.agro.machine.domain.MachineStatus;
import com.seuprojeto.agro.machine.domain.MachineType;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record MachineResponse(
        UUID id,
        UUID tenantId,
        String codigo,
        String nome,
        MachineType tipo,
        String fabricante,
        String modelo,
        Integer anoFabricacao,
        MachineStatus status,
        BigDecimal horimetroAtual,
        String observacoes,
        Instant createdAt,
        Instant updatedAt,
        String createdBy,
        String updatedBy
) {}

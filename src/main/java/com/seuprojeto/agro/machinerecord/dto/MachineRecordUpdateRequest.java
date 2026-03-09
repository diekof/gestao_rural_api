package com.seuprojeto.agro.machinerecord.dto;

import com.seuprojeto.agro.machinerecord.domain.MachineRecordType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record MachineRecordUpdateRequest(
        @NotNull UUID machineId,
        UUID operationId,
        @NotNull MachineRecordType tipo,
        @NotNull LocalDate dataRegistro,
        @DecimalMin(value = "0.00") BigDecimal horasTrabalhadas,
        @DecimalMin(value = "0.00") BigDecimal valor,
        String descricao
) {}

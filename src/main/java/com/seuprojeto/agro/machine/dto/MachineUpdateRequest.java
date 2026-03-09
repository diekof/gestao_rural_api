package com.seuprojeto.agro.machine.dto;

import com.seuprojeto.agro.machine.domain.MachineStatus;
import com.seuprojeto.agro.machine.domain.MachineType;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record MachineUpdateRequest(
        @NotBlank @Size(max = 60) String codigo,
        @NotBlank @Size(max = 140) String nome,
        @NotNull MachineType tipo,
        @Size(max = 100) String fabricante,
        @Size(max = 100) String modelo,
        @Min(1950) @Max(2100) Integer anoFabricacao,
        @NotNull MachineStatus status,
        @NotNull @DecimalMin(value = "0.00") BigDecimal horimetroAtual,
        String observacoes
) {}

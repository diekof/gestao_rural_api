package com.seuprojeto.agro.fuelsupply.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record FuelSupplyUpdateRequest(
        UUID machineId,
        @NotNull @DecimalMin(value = "0.01") BigDecimal valor,
        @NotNull @DecimalMin(value = "0.01") BigDecimal litros,
        @NotNull OffsetDateTime abastecidoEm,
        String localizacao,
        String observacao
) {}


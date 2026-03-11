package com.seuprojeto.agro.fuelcredit.dto;

import com.seuprojeto.agro.fuelcredit.domain.FuelCreditStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

public record FuelCreditCreateRequest(
        @NotNull UUID userId,
        @NotNull @DecimalMin(value = "0.00") BigDecimal creditLimit,
        @DecimalMin(value = "0.00") BigDecimal initialBalance,
        FuelCreditStatus status
) {}


package com.seuprojeto.agro.fuelcredit.dto;

import com.seuprojeto.agro.fuelcredit.domain.FuelCreditStatus;
import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;

public record FuelCreditUpdateRequest(
        @DecimalMin(value = "0.00") BigDecimal creditLimit,
        @DecimalMin(value = "0.00") BigDecimal rechargeAmount,
        FuelCreditStatus status
) {}


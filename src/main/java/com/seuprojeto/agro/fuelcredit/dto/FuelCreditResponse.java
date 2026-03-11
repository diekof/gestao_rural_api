package com.seuprojeto.agro.fuelcredit.dto;

import com.seuprojeto.agro.fuelcredit.domain.FuelCreditStatus;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record FuelCreditResponse(
        UUID id,
        UUID tenantId,
        UUID userId,
        String userName,
        FuelCreditStatus status,
        BigDecimal creditLimit,
        BigDecimal balance,
        BigDecimal totalConsumed,
        Instant lastRechargeAt,
        Instant createdAt,
        Instant updatedAt,
        String createdBy,
        String updatedBy
) {}


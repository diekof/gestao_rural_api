package com.seuprojeto.agro.financialentry.dto;

import com.seuprojeto.agro.financialentry.domain.FinancialCategory;
import com.seuprojeto.agro.financialentry.domain.FinancialEntryStatus;
import com.seuprojeto.agro.financialentry.domain.FinancialEntryType;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record FinancialEntryResponse(
        UUID id,
        UUID tenantId,
        UUID seasonId,
        UUID operationId,
        FinancialEntryType tipo,
        FinancialCategory categoria,
        FinancialEntryStatus status,
        LocalDate dataLancamento,
        LocalDate dataVencimento,
        LocalDate dataPagamento,
        BigDecimal valor,
        String descricao,
        String documento,
        Instant createdAt,
        Instant updatedAt,
        String createdBy,
        String updatedBy
) {}

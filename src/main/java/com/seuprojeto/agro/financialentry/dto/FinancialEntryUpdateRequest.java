package com.seuprojeto.agro.financialentry.dto;

import com.seuprojeto.agro.financialentry.domain.FinancialCategory;
import com.seuprojeto.agro.financialentry.domain.FinancialEntryStatus;
import com.seuprojeto.agro.financialentry.domain.FinancialEntryType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record FinancialEntryUpdateRequest(
        UUID seasonId,
        UUID operationId,
        @NotNull FinancialEntryType tipo,
        @NotNull FinancialCategory categoria,
        @NotNull FinancialEntryStatus status,
        @NotNull LocalDate dataLancamento,
        LocalDate dataVencimento,
        LocalDate dataPagamento,
        @NotNull @DecimalMin(value = "0.01") BigDecimal valor,
        @NotBlank @Size(max = 180) String descricao,
        @Size(max = 120) String documento
) {}

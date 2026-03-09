package com.seuprojeto.agro.agriculturaloperation.dto;

import com.seuprojeto.agro.agriculturaloperation.domain.OperationStatus;
import com.seuprojeto.agro.agriculturaloperation.domain.OperationType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record AgriculturalOperationCreateRequest(
        @NotNull UUID seasonId,
        @NotNull UUID fieldId,
        @NotNull OperationType tipo,
        OperationStatus status,
        @NotNull LocalDate dataOperacao,
        @NotNull @DecimalMin(value = "0.01") BigDecimal areaExecutada,
        @DecimalMin(value = "0.00") BigDecimal custoEstimado,
        @DecimalMin(value = "0.00") BigDecimal custoReal,
        String descricao
) {}

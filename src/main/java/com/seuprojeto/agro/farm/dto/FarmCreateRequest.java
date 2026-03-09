package com.seuprojeto.agro.farm.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record FarmCreateRequest(
        @NotBlank @Size(max = 150) String nome,
        @Size(max = 150) String proprietario,
        @NotNull @DecimalMin(value = "0.01") BigDecimal areaTotalHectares,
        @Size(max = 120) String cidade,
        @Size(min = 2, max = 2) String estado,
        @DecimalMin(value = "-90.0") @DecimalMax(value = "90.0") BigDecimal latitude,
        @DecimalMin(value = "-180.0") @DecimalMax(value = "180.0") BigDecimal longitude,
        String observacoes
) {}

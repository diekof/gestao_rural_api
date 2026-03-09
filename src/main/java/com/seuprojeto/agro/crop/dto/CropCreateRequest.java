package com.seuprojeto.agro.crop.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record CropCreateRequest(
        @NotBlank @Size(max = 120) String nome,
        @Size(max = 80) String categoria,
        @NotNull @Min(1) Integer cicloMedioDias,
        @DecimalMin(value = "0.01") BigDecimal produtividadeEsperada,
        @Size(max = 30) String unidadeProdutividade,
        String descricao
) {}

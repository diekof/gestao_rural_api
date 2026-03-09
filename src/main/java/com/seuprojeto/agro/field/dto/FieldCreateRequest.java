package com.seuprojeto.agro.field.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.UUID;

public record FieldCreateRequest(
        @NotNull UUID farmId,
        @NotBlank @Size(max = 150) String nome,
        @Size(max = 50) String codigo,
        @NotNull @DecimalMin(value = "0.01") BigDecimal areaHectares,
        @Size(max = 80) String tipoSolo,
        String geoJson,
        String observacoes
) {}

package com.seuprojeto.agro.tenant.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TenantRequest(
        @NotBlank @Size(max = 150) String nome,
        @NotBlank @Size(max = 150) String nomeFantasia,
        @NotBlank @Size(max = 20) String cpfCnpj,
        @NotBlank @Email @Size(max = 150) String email,
        @Size(max = 20) String telefone,
        @NotBlank @Size(max = 50) String plano
) {}

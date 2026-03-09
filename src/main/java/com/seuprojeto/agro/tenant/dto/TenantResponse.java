package com.seuprojeto.agro.tenant.dto;

import com.seuprojeto.agro.common.Status;
import java.time.OffsetDateTime;
import java.util.UUID;

public record TenantResponse(
        UUID id,
        String nome,
        String nomeFantasia,
        String cpfCnpj,
        String email,
        String telefone,
        String plano,
        Status status,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        String createdBy,
        String updatedBy
) {}

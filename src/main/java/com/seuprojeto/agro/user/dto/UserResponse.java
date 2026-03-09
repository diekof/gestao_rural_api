package com.seuprojeto.agro.user.dto;

import com.seuprojeto.agro.common.Role;
import com.seuprojeto.agro.common.Status;
import java.time.OffsetDateTime;
import java.util.UUID;

public record UserResponse(
        UUID id,
        UUID tenantId,
        String nome,
        String email,
        String username,
        Role role,
        Status status,
        OffsetDateTime lastLoginAt,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        String createdBy,
        String updatedBy
) {}

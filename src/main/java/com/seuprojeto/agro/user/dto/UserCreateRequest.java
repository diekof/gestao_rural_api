package com.seuprojeto.agro.user.dto;

import com.seuprojeto.agro.common.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record UserCreateRequest(
        UUID tenantId,
        @NotBlank @Size(max = 120) String nome,
        @NotBlank @Email @Size(max = 150) String email,
        @NotBlank @Size(max = 50) String username,
        @NotBlank @Size(min = 8, max = 120) String password,
        @NotNull Role role
) {}

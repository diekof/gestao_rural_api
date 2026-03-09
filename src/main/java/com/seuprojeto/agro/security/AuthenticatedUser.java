package com.seuprojeto.agro.security;

import com.seuprojeto.agro.common.Role;
import java.util.UUID;

public record AuthenticatedUser(UUID userId, UUID tenantId, String username, Role role) {
    public boolean isSuperAdmin() {
        return Role.SUPER_ADMIN.equals(role);
    }
}

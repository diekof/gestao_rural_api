package com.seuprojeto.agro.user.mapper;

import com.seuprojeto.agro.user.domain.User;
import com.seuprojeto.agro.user.dto.UserResponse;
import com.seuprojeto.agro.user.dto.UserUpdateRequest;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public void updateEntity(User user, UserUpdateRequest request) {
        user.setNome(request.nome());
        user.setEmail(request.email());
        user.setUsername(request.username());
        user.setRole(request.role());
    }

    public UserResponse toResponse(User user) {
        return new UserResponse(user.getId(), user.getTenantId(), user.getNome(), user.getEmail(), user.getUsername(),
                user.getRole(), user.getStatus(), user.getLastLoginAt(), user.getCreatedAt(), user.getUpdatedAt(),
                user.getCreatedBy(), user.getUpdatedBy());
    }
}

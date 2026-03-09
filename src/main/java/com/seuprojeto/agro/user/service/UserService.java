package com.seuprojeto.agro.user.service;

import com.seuprojeto.agro.common.Role;
import com.seuprojeto.agro.common.Status;
import com.seuprojeto.agro.exception.ConflictException;
import com.seuprojeto.agro.exception.ForbiddenException;
import com.seuprojeto.agro.exception.NotFoundException;
import com.seuprojeto.agro.exception.UnauthorizedException;
import com.seuprojeto.agro.security.AuthenticatedUser;
import com.seuprojeto.agro.security.TenantContext;
import com.seuprojeto.agro.tenant.domain.Tenant;
import com.seuprojeto.agro.tenant.repository.TenantRepository;
import com.seuprojeto.agro.user.domain.User;
import com.seuprojeto.agro.user.dto.ChangePasswordRequest;
import com.seuprojeto.agro.user.dto.UserCreateRequest;
import com.seuprojeto.agro.user.dto.UserResponse;
import com.seuprojeto.agro.user.dto.UserUpdateRequest;
import com.seuprojeto.agro.user.mapper.UserMapper;
import com.seuprojeto.agro.user.repository.UserRepository;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final TenantRepository tenantRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, TenantRepository tenantRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tenantRepository = tenantRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponse create(UserCreateRequest request) {
        AuthenticatedUser current = getCurrentUser();
        UUID tenantId = resolveTenantId(current, request.tenantId());
        validateTenant(tenantId);
        if (userRepository.existsByEmailIgnoreCaseAndTenantId(request.email(), tenantId)) throw new ConflictException("Email already exists");
        if (userRepository.existsByUsernameIgnoreCaseAndTenantId(request.username(), tenantId)) throw new ConflictException("Username already exists");

        User user = new User();
        user.setTenantId(tenantId);
        user.setNome(request.nome());
        user.setEmail(request.email());
        user.setUsername(request.username());
        user.setRole(request.role());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        return userMapper.toResponse(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public UserResponse findById(UUID id) {
        User user = getUser(id);
        enforceTenantAccess(getCurrentUser(), user.getTenantId());
        return userMapper.toResponse(user);
    }

    @Transactional(readOnly = true)
    public Page<UserResponse> list(Pageable pageable) {
        AuthenticatedUser current = getCurrentUser();
        if (current.isSuperAdmin()) return userRepository.findAll(pageable).map(userMapper::toResponse);
        if (current.role() != Role.TENANT_ADMIN) throw new ForbiddenException("Not allowed to list users");
        return userRepository.findByTenantId(current.tenantId(), pageable).map(userMapper::toResponse);
    }

    @Transactional
    public UserResponse update(UUID id, UserUpdateRequest request) {
        User user = getUser(id);
        AuthenticatedUser current = getCurrentUser();
        enforceTenantAccess(current, user.getTenantId());
        userMapper.updateEntity(user, request);
        return userMapper.toResponse(userRepository.save(user));
    }

    @Transactional
    public void activate(UUID id) { changeStatus(id, Status.ACTIVE); }
    @Transactional
    public void deactivate(UUID id) { changeStatus(id, Status.INACTIVE); }

    @Transactional
    public void changePassword(UUID id, ChangePasswordRequest request) {
        User user = getUser(id);
        enforceTenantAccess(getCurrentUser(), user.getTenantId());
        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public UserResponse me() {
        return userMapper.toResponse(getUser(getCurrentUser().userId()));
    }

    private void changeStatus(UUID id, Status status) {
        User user = getUser(id);
        enforceTenantAccess(getCurrentUser(), user.getTenantId());
        user.setStatus(status);
        userRepository.save(user);
    }

    private UUID resolveTenantId(AuthenticatedUser current, UUID requestTenantId) {
        if (current.isSuperAdmin()) {
            if (requestTenantId == null) throw new IllegalArgumentException("tenantId is required for SUPER_ADMIN");
            return requestTenantId;
        }
        if (current.role() != Role.TENANT_ADMIN) throw new ForbiddenException("Not allowed to create users");
        return current.tenantId();
    }

    private void enforceTenantAccess(AuthenticatedUser current, UUID tenantId) {
        if (current.isSuperAdmin()) return;
        if (!tenantId.equals(TenantContext.getTenantId())) throw new ForbiddenException("Cross-tenant access denied");
    }

    private void validateTenant(UUID tenantId) {
        Tenant tenant = tenantRepository.findById(tenantId).orElseThrow(() -> new NotFoundException("Tenant not found"));
        if (tenant.getStatus() != Status.ACTIVE) throw new ForbiddenException("Tenant inactive");
    }

    private User getUser(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
    }

    private AuthenticatedUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof AuthenticatedUser authUser)) {
            throw new UnauthorizedException("Unauthenticated");
        }
        return authUser;
    }
}

package com.seuprojeto.agro.auth.service;

import com.seuprojeto.agro.auth.dto.AuthResponse;
import com.seuprojeto.agro.auth.dto.LoginRequest;
import com.seuprojeto.agro.auth.dto.RefreshRequest;
import com.seuprojeto.agro.common.Status;
import com.seuprojeto.agro.exception.UnauthorizedException;
import com.seuprojeto.agro.security.AuthenticatedUser;
import com.seuprojeto.agro.security.JwtService;
import com.seuprojeto.agro.user.domain.User;
import com.seuprojeto.agro.user.dto.UserResponse;
import com.seuprojeto.agro.user.mapper.UserMapper;
import com.seuprojeto.agro.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import java.time.OffsetDateTime;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userMapper = userMapper;
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmailIgnoreCase(request.usernameOrEmail())
                .or(() -> userRepository.findByUsernameIgnoreCase(request.usernameOrEmail()))
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));
        if (user.getStatus() != Status.ACTIVE || !passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new UnauthorizedException("Invalid credentials");
        }
        user.setLastLoginAt(OffsetDateTime.now());
        userRepository.save(user);
        AuthenticatedUser authUser = new AuthenticatedUser(user.getId(), user.getTenantId(), user.getUsername(), user.getRole());
        return new AuthResponse(jwtService.generateAccessToken(authUser), jwtService.generateRefreshToken(authUser));
    }

    public AuthResponse refresh(RefreshRequest request) {
        Claims claims = jwtService.parse(request.refreshToken());
        if (!"refresh".equals(claims.get("type", String.class))) throw new UnauthorizedException("Invalid refresh token");
        User user = userRepository.findById(java.util.UUID.fromString(claims.getSubject()))
                .orElseThrow(() -> new UnauthorizedException("Invalid refresh token"));
        AuthenticatedUser authUser = new AuthenticatedUser(user.getId(), user.getTenantId(), user.getUsername(), user.getRole());
        return new AuthResponse(jwtService.generateAccessToken(authUser), jwtService.generateRefreshToken(authUser));
    }

    public UserResponse me(AuthenticatedUser user) {
        User loaded = userRepository.findById(user.userId()).orElseThrow(() -> new UnauthorizedException("User not found"));
        return userMapper.toResponse(loaded);
    }
}

package com.seuprojeto.agro.user.repository;

import com.seuprojeto.agro.user.domain.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmailIgnoreCase(String email);
    Optional<User> findByUsernameIgnoreCase(String username);
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByUsernameIgnoreCase(String username);
    boolean existsByEmailIgnoreCaseAndTenantId(String email, UUID tenantId);
    boolean existsByUsernameIgnoreCaseAndTenantId(String username, UUID tenantId);
    Page<User> findByTenantId(UUID tenantId, Pageable pageable);
}

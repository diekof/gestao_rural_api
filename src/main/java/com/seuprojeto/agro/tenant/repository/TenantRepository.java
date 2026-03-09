package com.seuprojeto.agro.tenant.repository;

import com.seuprojeto.agro.tenant.domain.Tenant;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantRepository extends JpaRepository<Tenant, UUID> {
    boolean existsByCpfCnpj(String cpfCnpj);
    boolean existsByEmail(String email);
    Optional<Tenant> findByEmail(String email);
}

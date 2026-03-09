package com.seuprojeto.agro.farm.repository;

import com.seuprojeto.agro.farm.domain.Farm;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FarmRepository extends JpaRepository<Farm, UUID>, JpaSpecificationExecutor<Farm> {
    boolean existsByTenantIdAndNomeIgnoreCase(UUID tenantId, String nome);
}

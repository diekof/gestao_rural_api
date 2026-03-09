package com.seuprojeto.agro.crop.repository;

import com.seuprojeto.agro.crop.domain.Crop;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CropRepository extends JpaRepository<Crop, UUID>, JpaSpecificationExecutor<Crop> {
    boolean existsByTenantIdAndNomeIgnoreCase(UUID tenantId, String nome);
    boolean existsByTenantIdAndNomeIgnoreCaseAndIdNot(UUID tenantId, String nome, UUID id);
}

package com.seuprojeto.agro.fuelcredit.repository;

import com.seuprojeto.agro.fuelcredit.domain.FuelCredit;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FuelCreditRepository extends JpaRepository<FuelCredit, UUID>, JpaSpecificationExecutor<FuelCredit> {
    boolean existsByTenantIdAndUserId(UUID tenantId, UUID userId);
    Optional<FuelCredit> findByTenantIdAndUserId(UUID tenantId, UUID userId);
}


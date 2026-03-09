package com.seuprojeto.agro.machine.repository;

import com.seuprojeto.agro.machine.domain.Machine;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MachineRepository extends JpaRepository<Machine, UUID>, JpaSpecificationExecutor<Machine> {
    boolean existsByTenantIdAndCodigoIgnoreCase(UUID tenantId, String codigo);
    boolean existsByTenantIdAndCodigoIgnoreCaseAndIdNot(UUID tenantId, String codigo, UUID id);
}

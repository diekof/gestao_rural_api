package com.seuprojeto.agro.machinerecord.repository;

import com.seuprojeto.agro.machinerecord.domain.MachineRecord;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MachineRecordRepository extends JpaRepository<MachineRecord, UUID>, JpaSpecificationExecutor<MachineRecord> {
    boolean existsByMachineId(UUID machineId);
}

package com.seuprojeto.agro.field.repository;

import com.seuprojeto.agro.common.Status;
import com.seuprojeto.agro.field.domain.Field;
import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface FieldRepository extends JpaRepository<Field, UUID>, JpaSpecificationExecutor<Field> {
    boolean existsByFarmIdAndCodigoIgnoreCase(UUID farmId, String codigo);
    boolean existsByFarmIdAndCodigoIgnoreCaseAndIdNot(UUID farmId, String codigo, UUID id);

    @Query("select coalesce(sum(f.areaHectares), 0) from Field f where f.farmId = :farmId and f.status = :status")
    BigDecimal sumAreaByFarmIdAndStatus(UUID farmId, Status status);

    @Query("select coalesce(sum(f.areaHectares), 0) from Field f where f.farmId = :farmId and f.status = :status and f.id <> :fieldId")
    BigDecimal sumAreaByFarmIdAndStatusExcludingField(UUID farmId, Status status, UUID fieldId);

    long countByFarmId(UUID farmId);
}

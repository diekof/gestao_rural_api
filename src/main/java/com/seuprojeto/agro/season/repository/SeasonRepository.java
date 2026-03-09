package com.seuprojeto.agro.season.repository;

import com.seuprojeto.agro.season.domain.Season;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SeasonRepository extends JpaRepository<Season, UUID>, JpaSpecificationExecutor<Season> {
    boolean existsByTenantIdAndFieldIdAndNomeIgnoreCaseAndAnoSafra(UUID tenantId, UUID fieldId, String nome, Integer anoSafra);
    boolean existsByTenantIdAndFieldIdAndNomeIgnoreCaseAndAnoSafraAndIdNot(UUID tenantId, UUID fieldId, String nome, Integer anoSafra, UUID id);
    boolean existsByFieldId(UUID fieldId);
    boolean existsByCropId(UUID cropId);
}

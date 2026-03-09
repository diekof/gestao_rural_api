package com.seuprojeto.agro.agriculturaloperation.repository;

import com.seuprojeto.agro.agriculturaloperation.domain.AgriculturalOperation;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AgriculturalOperationRepository extends JpaRepository<AgriculturalOperation, UUID>, JpaSpecificationExecutor<AgriculturalOperation> {
    boolean existsByTenantIdAndSeasonIdAndDataOperacaoAndTipo(UUID tenantId, UUID seasonId, java.time.LocalDate dataOperacao,
                                                               com.seuprojeto.agro.agriculturaloperation.domain.OperationType tipo);
    boolean existsByTenantIdAndSeasonIdAndDataOperacaoAndTipoAndIdNot(UUID tenantId, UUID seasonId, java.time.LocalDate dataOperacao,
                                                                       com.seuprojeto.agro.agriculturaloperation.domain.OperationType tipo,
                                                                       UUID id);
}

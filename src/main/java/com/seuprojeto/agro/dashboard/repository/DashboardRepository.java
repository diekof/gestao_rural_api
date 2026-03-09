package com.seuprojeto.agro.dashboard.repository;

import com.seuprojeto.agro.financialentry.domain.FinancialEntryStatus;
import com.seuprojeto.agro.financialentry.domain.FinancialEntryType;
import com.seuprojeto.agro.financialentry.domain.FinancialCategory;
import com.seuprojeto.agro.agriculturaloperation.domain.OperationStatus;
import com.seuprojeto.agro.agriculturaloperation.domain.OperationType;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface DashboardRepository extends Repository<com.seuprojeto.agro.farm.domain.Farm, UUID> {

    @Query("select count(f.id) from Farm f where (:tenantId is null or f.tenantId = :tenantId)")
    long countFarms(@Param("tenantId") UUID tenantId);

    @Query("select count(f.id) from Field f where (:tenantId is null or f.tenantId = :tenantId)")
    long countFields(@Param("tenantId") UUID tenantId);

    @Query("select count(s.id) from Season s where (:tenantId is null or s.tenantId = :tenantId) and s.status = com.seuprojeto.agro.season.domain.SeasonStatus.IN_PROGRESS")
    long countActiveSeasons(@Param("tenantId") UUID tenantId);

    @Query("""
            select count(o.id) from AgriculturalOperation o
            where (:tenantId is null or o.tenantId = :tenantId)
              and EXTRACT(MONTH FROM o.dataOperacao) = EXTRACT(MONTH FROM CURRENT_DATE)
              and EXTRACT(YEAR FROM o.dataOperacao) = EXTRACT(YEAR FROM CURRENT_DATE)
            """)
    long countOperationsCurrentMonth(@Param("tenantId") UUID tenantId);

    @Query("""
            select coalesce(sum(fe.valor), 0) from FinancialEntry fe
            where (:tenantId is null or fe.tenantId = :tenantId)
              and fe.tipo = :tipo
            """)
    BigDecimal sumFinancialByType(@Param("tenantId") UUID tenantId, @Param("tipo") FinancialEntryType tipo);

    @Query("""
            select coalesce(sum(fe.valor), 0) from FinancialEntry fe
            where (:tenantId is null or fe.tenantId = :tenantId)
              and fe.status = :status
            """)
    BigDecimal sumFinancialByStatus(@Param("tenantId") UUID tenantId, @Param("status") FinancialEntryStatus status);

    @Query("""
            select fe.categoria as categoria, coalesce(sum(fe.valor),0) as total
            from FinancialEntry fe
            where (:tenantId is null or fe.tenantId = :tenantId)
            group by fe.categoria
            """)
    List<FinancialByCategoryProjection> financialByCategory(@Param("tenantId") UUID tenantId);

    @Query("""
            select coalesce(sum(s.areaPlantada),0), coalesce(sum(s.previsaoProducao),0), coalesce(sum(s.producaoReal),0)
            from Season s
            where (:tenantId is null or s.tenantId = :tenantId)
            """)
    Object productionTotals(@Param("tenantId") UUID tenantId);

    @Query("""
            select c.nome as cropName,
                   coalesce(sum(s.areaPlantada),0) as plantedArea,
                   coalesce(sum(s.previsaoProducao),0) as expectedProduction,
                   coalesce(sum(s.producaoReal),0) as actualProduction
            from Season s, Crop c
            where c.id = s.cropId
              and (:tenantId is null or s.tenantId = :tenantId)
            group by c.nome
            """)
    List<ProductionByCropProjection> productionByCrop(@Param("tenantId") UUID tenantId);

    @Query("select count(o.id) from AgriculturalOperation o where (:tenantId is null or o.tenantId = :tenantId)")
    long countOperations(@Param("tenantId") UUID tenantId);

    @Query("""
            select count(o.id) from AgriculturalOperation o
            where (:tenantId is null or o.tenantId = :tenantId)
              and o.status = :status
            """)
    long countOperationsByStatus(@Param("tenantId") UUID tenantId, @Param("status") OperationStatus status);

    @Query("""
            select coalesce(sum(o.areaExecutada),0), coalesce(sum(o.custoEstimado),0), coalesce(sum(o.custoReal),0)
            from AgriculturalOperation o
            where (:tenantId is null or o.tenantId = :tenantId)
            """)
    Object operationTotals(@Param("tenantId") UUID tenantId);

    @Query("""
            select o.tipo as tipo,
                   count(o.id) as total,
                   coalesce(sum(o.areaExecutada),0) as area,
                   coalesce(sum(o.custoReal),0) as cost
            from AgriculturalOperation o
            where (:tenantId is null or o.tenantId = :tenantId)
            group by o.tipo
            """)
    List<OperationsByTypeProjection> operationsByType(@Param("tenantId") UUID tenantId);

    interface FinancialByCategoryProjection {
        FinancialCategory getCategoria();
        BigDecimal getTotal();
    }

    interface ProductionByCropProjection {
        String getCropName();
        BigDecimal getPlantedArea();
        BigDecimal getExpectedProduction();
        BigDecimal getActualProduction();
    }

    interface OperationsByTypeProjection {
        OperationType getTipo();
        long getTotal();
        BigDecimal getArea();
        BigDecimal getCost();
    }
}

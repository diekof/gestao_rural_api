package com.seuprojeto.agro.satellite.repository;

import com.seuprojeto.agro.satellite.domain.SatelliteImage;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SatelliteImageRepository extends JpaRepository<SatelliteImage, UUID> {

    @Query("""
            select s from SatelliteImage s
            where (:tenantId is null or s.tenantId = :tenantId)
              and (:fieldId is null or s.fieldId = :fieldId)
            order by s.capturedAt desc
            """)
    Page<SatelliteImage> findByFilters(@Param("tenantId") UUID tenantId,
                                       @Param("fieldId") UUID fieldId,
                                       Pageable pageable);

    @Query("""
            select s from SatelliteImage s
            where (:tenantId is null or s.tenantId = :tenantId)
              and (:fieldId is null or s.fieldId = :fieldId)
              and (:startDate is null or s.capturedAt >= :startDate)
              and (:endDate is null or s.capturedAt <= :endDate)
            order by s.capturedAt asc
            """)
    List<SatelliteImage> findNdviHistory(@Param("tenantId") UUID tenantId,
                                         @Param("fieldId") UUID fieldId,
                                         @Param("startDate") LocalDateTime startDate,
                                         @Param("endDate") LocalDateTime endDate);
}

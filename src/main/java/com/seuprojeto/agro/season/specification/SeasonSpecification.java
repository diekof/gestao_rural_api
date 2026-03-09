package com.seuprojeto.agro.season.specification;

import com.seuprojeto.agro.season.domain.Season;
import com.seuprojeto.agro.season.domain.SeasonStatus;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;

public final class SeasonSpecification {
    private SeasonSpecification() {}

    public static Specification<Season> filter(UUID tenantId, UUID fieldId, UUID cropId, Integer anoSafra, SeasonStatus status,
                                               LocalDate dataInicioFrom, LocalDate dataInicioTo) {
        return (root, query, cb) -> cb.and(
                tenantId == null ? cb.conjunction() : cb.equal(root.get("tenantId"), tenantId),
                fieldId == null ? cb.conjunction() : cb.equal(root.get("fieldId"), fieldId),
                cropId == null ? cb.conjunction() : cb.equal(root.get("cropId"), cropId),
                anoSafra == null ? cb.conjunction() : cb.equal(root.get("anoSafra"), anoSafra),
                status == null ? cb.conjunction() : cb.equal(root.get("status"), status),
                dataInicioFrom == null ? cb.conjunction() : cb.greaterThanOrEqualTo(root.get("dataInicio"), dataInicioFrom),
                dataInicioTo == null ? cb.conjunction() : cb.lessThanOrEqualTo(root.get("dataInicio"), dataInicioTo)
        );
    }
}

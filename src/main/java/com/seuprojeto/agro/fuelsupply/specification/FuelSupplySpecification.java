package com.seuprojeto.agro.fuelsupply.specification;

import com.seuprojeto.agro.fuelsupply.domain.FuelSupply;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;

public final class FuelSupplySpecification {

    private FuelSupplySpecification() {}

    public static Specification<FuelSupply> filter(UUID tenantId, UUID userId, UUID machineId,
                                                   OffsetDateTime dataInicio, OffsetDateTime dataFim) {
        return (root, query, cb) -> cb.and(
                tenantId == null ? cb.conjunction() : cb.equal(root.get("tenantId"), tenantId),
                userId == null ? cb.conjunction() : cb.equal(root.get("userId"), userId),
                machineId == null ? cb.conjunction() : cb.equal(root.get("machineId"), machineId),
                dataInicio == null ? cb.conjunction() : cb.greaterThanOrEqualTo(root.get("abastecidoEm"), dataInicio),
                dataFim == null ? cb.conjunction() : cb.lessThanOrEqualTo(root.get("abastecidoEm"), dataFim)
        );
    }
}


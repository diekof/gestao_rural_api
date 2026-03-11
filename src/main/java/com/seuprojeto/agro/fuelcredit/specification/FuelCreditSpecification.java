package com.seuprojeto.agro.fuelcredit.specification;

import com.seuprojeto.agro.fuelcredit.domain.FuelCredit;
import com.seuprojeto.agro.fuelcredit.domain.FuelCreditStatus;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;

public final class FuelCreditSpecification {

    private FuelCreditSpecification() {}

    public static Specification<FuelCredit> filter(UUID tenantId, UUID userId, FuelCreditStatus status) {
        return (root, query, cb) -> cb.and(
                tenantId == null ? cb.conjunction() : cb.equal(root.get("tenantId"), tenantId),
                userId == null ? cb.conjunction() : cb.equal(root.get("userId"), userId),
                status == null ? cb.conjunction() : cb.equal(root.get("status"), status)
        );
    }
}


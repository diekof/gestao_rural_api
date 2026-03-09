package com.seuprojeto.agro.agriculturaloperation.specification;

import com.seuprojeto.agro.agriculturaloperation.domain.AgriculturalOperation;
import com.seuprojeto.agro.agriculturaloperation.domain.OperationStatus;
import com.seuprojeto.agro.agriculturaloperation.domain.OperationType;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;

public final class AgriculturalOperationSpecification {

    private AgriculturalOperationSpecification() {}

    public static Specification<AgriculturalOperation> filter(UUID tenantId, UUID seasonId, UUID fieldId, OperationType tipo,
                                                              OperationStatus status, LocalDate dataInicio, LocalDate dataFim) {
        return (root, query, cb) -> cb.and(
                tenantId == null ? cb.conjunction() : cb.equal(root.get("tenantId"), tenantId),
                seasonId == null ? cb.conjunction() : cb.equal(root.get("seasonId"), seasonId),
                fieldId == null ? cb.conjunction() : cb.equal(root.get("fieldId"), fieldId),
                tipo == null ? cb.conjunction() : cb.equal(root.get("tipo"), tipo),
                status == null ? cb.conjunction() : cb.equal(root.get("status"), status),
                dataInicio == null ? cb.conjunction() : cb.greaterThanOrEqualTo(root.get("dataOperacao"), dataInicio),
                dataFim == null ? cb.conjunction() : cb.lessThanOrEqualTo(root.get("dataOperacao"), dataFim)
        );
    }
}

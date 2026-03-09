package com.seuprojeto.agro.financialentry.specification;

import com.seuprojeto.agro.financialentry.domain.FinancialCategory;
import com.seuprojeto.agro.financialentry.domain.FinancialEntry;
import com.seuprojeto.agro.financialentry.domain.FinancialEntryStatus;
import com.seuprojeto.agro.financialentry.domain.FinancialEntryType;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;

public final class FinancialEntrySpecification {

    private FinancialEntrySpecification() {}

    public static Specification<FinancialEntry> filter(UUID tenantId, UUID seasonId, UUID operationId,
                                                       FinancialEntryType tipo, FinancialCategory categoria,
                                                       FinancialEntryStatus status, LocalDate dataInicio,
                                                       LocalDate dataFim) {
        return (root, query, cb) -> cb.and(
                tenantId == null ? cb.conjunction() : cb.equal(root.get("tenantId"), tenantId),
                seasonId == null ? cb.conjunction() : cb.equal(root.get("seasonId"), seasonId),
                operationId == null ? cb.conjunction() : cb.equal(root.get("operationId"), operationId),
                tipo == null ? cb.conjunction() : cb.equal(root.get("tipo"), tipo),
                categoria == null ? cb.conjunction() : cb.equal(root.get("categoria"), categoria),
                status == null ? cb.conjunction() : cb.equal(root.get("status"), status),
                dataInicio == null ? cb.conjunction() : cb.greaterThanOrEqualTo(root.get("dataLancamento"), dataInicio),
                dataFim == null ? cb.conjunction() : cb.lessThanOrEqualTo(root.get("dataLancamento"), dataFim)
        );
    }
}

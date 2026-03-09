package com.seuprojeto.agro.machinerecord.specification;

import com.seuprojeto.agro.machinerecord.domain.MachineRecord;
import com.seuprojeto.agro.machinerecord.domain.MachineRecordType;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;

public final class MachineRecordSpecification {

    private MachineRecordSpecification() {}

    public static Specification<MachineRecord> filter(UUID tenantId, UUID machineId, UUID operationId, MachineRecordType tipo,
                                                      LocalDate dataInicio, LocalDate dataFim) {
        return (root, query, cb) -> cb.and(
                tenantId == null ? cb.conjunction() : cb.equal(root.get("tenantId"), tenantId),
                machineId == null ? cb.conjunction() : cb.equal(root.get("machineId"), machineId),
                operationId == null ? cb.conjunction() : cb.equal(root.get("operationId"), operationId),
                tipo == null ? cb.conjunction() : cb.equal(root.get("tipo"), tipo),
                dataInicio == null ? cb.conjunction() : cb.greaterThanOrEqualTo(root.get("dataRegistro"), dataInicio),
                dataFim == null ? cb.conjunction() : cb.lessThanOrEqualTo(root.get("dataRegistro"), dataFim)
        );
    }
}

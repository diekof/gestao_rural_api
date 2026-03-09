package com.seuprojeto.agro.machine.specification;

import com.seuprojeto.agro.machine.domain.Machine;
import com.seuprojeto.agro.machine.domain.MachineStatus;
import com.seuprojeto.agro.machine.domain.MachineType;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;

public final class MachineSpecification {

    private MachineSpecification() {}

    public static Specification<Machine> filter(UUID tenantId, String termo, MachineType tipo, MachineStatus status) {
        return (root, query, cb) -> cb.and(
                tenantId == null ? cb.conjunction() : cb.equal(root.get("tenantId"), tenantId),
                termo == null ? cb.conjunction() : cb.or(
                        cb.like(cb.lower(root.get("codigo")), "%" + termo.toLowerCase() + "%"),
                        cb.like(cb.lower(root.get("nome")), "%" + termo.toLowerCase() + "%")
                ),
                tipo == null ? cb.conjunction() : cb.equal(root.get("tipo"), tipo),
                status == null ? cb.conjunction() : cb.equal(root.get("status"), status)
        );
    }
}

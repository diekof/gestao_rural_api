package com.seuprojeto.agro.field.specification;

import com.seuprojeto.agro.common.Status;
import com.seuprojeto.agro.field.domain.Field;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;

public final class FieldSpecification {
    private FieldSpecification() {}

    public static Specification<Field> filter(UUID tenantId, UUID farmId, String nome, Status status, String tipoSolo) {
        return (root, query, cb) -> cb.and(
                tenantId == null ? cb.conjunction() : cb.equal(root.get("tenantId"), tenantId),
                farmId == null ? cb.conjunction() : cb.equal(root.get("farmId"), farmId),
                nome == null ? cb.conjunction() : cb.like(cb.lower(root.get("nome")), "%" + nome.toLowerCase() + "%"),
                status == null ? cb.conjunction() : cb.equal(root.get("status"), status),
                tipoSolo == null ? cb.conjunction() : cb.like(cb.lower(root.get("tipoSolo")), "%" + tipoSolo.toLowerCase() + "%")
        );
    }
}

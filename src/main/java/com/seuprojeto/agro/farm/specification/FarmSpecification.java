package com.seuprojeto.agro.farm.specification;

import com.seuprojeto.agro.common.Status;
import com.seuprojeto.agro.farm.domain.Farm;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;

public final class FarmSpecification {
    private FarmSpecification() {}

    public static Specification<Farm> filter(UUID tenantId, String nome, String cidade, String estado, Status status) {
        return (root, query, cb) -> cb.and(
                tenantId == null ? cb.conjunction() : cb.equal(root.get("tenantId"), tenantId),
                nome == null ? cb.conjunction() : cb.like(cb.lower(root.get("nome")), "%" + nome.toLowerCase() + "%"),
                cidade == null ? cb.conjunction() : cb.like(cb.lower(root.get("cidade")), "%" + cidade.toLowerCase() + "%"),
                estado == null ? cb.conjunction() : cb.equal(cb.upper(root.get("estado")), estado.toUpperCase()),
                status == null ? cb.conjunction() : cb.equal(root.get("status"), status)
        );
    }
}

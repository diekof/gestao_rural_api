package com.seuprojeto.agro.crop.specification;

import com.seuprojeto.agro.common.Status;
import com.seuprojeto.agro.crop.domain.Crop;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;

public final class CropSpecification {
    private CropSpecification() {}

    public static Specification<Crop> filter(UUID tenantId, String nome, String categoria, Status status) {
        return (root, query, cb) -> cb.and(
                tenantId == null ? cb.conjunction() : cb.equal(root.get("tenantId"), tenantId),
                nome == null ? cb.conjunction() : cb.like(cb.lower(root.get("nome")), "%" + nome.toLowerCase() + "%"),
                categoria == null ? cb.conjunction() : cb.like(cb.lower(root.get("categoria")), "%" + categoria.toLowerCase() + "%"),
                status == null ? cb.conjunction() : cb.equal(root.get("status"), status)
        );
    }
}

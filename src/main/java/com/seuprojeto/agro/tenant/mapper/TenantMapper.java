package com.seuprojeto.agro.tenant.mapper;

import com.seuprojeto.agro.tenant.domain.Tenant;
import com.seuprojeto.agro.tenant.dto.TenantRequest;
import com.seuprojeto.agro.tenant.dto.TenantResponse;
import org.springframework.stereotype.Component;

@Component
public class TenantMapper {
    public Tenant toEntity(TenantRequest request) {
        Tenant tenant = new Tenant();
        tenant.setNome(request.nome());
        tenant.setNomeFantasia(request.nomeFantasia());
        tenant.setCpfCnpj(request.cpfCnpj());
        tenant.setEmail(request.email());
        tenant.setTelefone(request.telefone());
        tenant.setPlano(request.plano());
        return tenant;
    }

    public void updateEntity(Tenant tenant, TenantRequest request) {
        tenant.setNome(request.nome());
        tenant.setNomeFantasia(request.nomeFantasia());
        tenant.setCpfCnpj(request.cpfCnpj());
        tenant.setEmail(request.email());
        tenant.setTelefone(request.telefone());
        tenant.setPlano(request.plano());
    }

    public TenantResponse toResponse(Tenant tenant) {
        return new TenantResponse(tenant.getId(), tenant.getNome(), tenant.getNomeFantasia(), tenant.getCpfCnpj(), tenant.getEmail(),
                tenant.getTelefone(), tenant.getPlano(), tenant.getStatus(), tenant.getCreatedAt(), tenant.getUpdatedAt(),
                tenant.getCreatedBy(), tenant.getUpdatedBy());
    }
}

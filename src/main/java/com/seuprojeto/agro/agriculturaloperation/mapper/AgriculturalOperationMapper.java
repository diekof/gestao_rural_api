package com.seuprojeto.agro.agriculturaloperation.mapper;

import com.seuprojeto.agro.agriculturaloperation.domain.AgriculturalOperation;
import com.seuprojeto.agro.agriculturaloperation.dto.AgriculturalOperationCreateRequest;
import com.seuprojeto.agro.agriculturaloperation.dto.AgriculturalOperationResponse;
import com.seuprojeto.agro.agriculturaloperation.dto.AgriculturalOperationUpdateRequest;
import org.springframework.stereotype.Component;

@Component
public class AgriculturalOperationMapper {

    public AgriculturalOperation toEntity(AgriculturalOperationCreateRequest request) {
        AgriculturalOperation op = new AgriculturalOperation();
        op.setSeasonId(request.seasonId());
        op.setFieldId(request.fieldId());
        op.setTipo(request.tipo());
        op.setStatus(request.status());
        op.setDataOperacao(request.dataOperacao());
        op.setAreaExecutada(request.areaExecutada());
        op.setCustoEstimado(request.custoEstimado());
        op.setCustoReal(request.custoReal());
        op.setDescricao(request.descricao());
        return op;
    }

    public void updateEntity(AgriculturalOperation op, AgriculturalOperationUpdateRequest request) {
        op.setSeasonId(request.seasonId());
        op.setFieldId(request.fieldId());
        op.setTipo(request.tipo());
        op.setStatus(request.status());
        op.setDataOperacao(request.dataOperacao());
        op.setAreaExecutada(request.areaExecutada());
        op.setCustoEstimado(request.custoEstimado());
        op.setCustoReal(request.custoReal());
        op.setDescricao(request.descricao());
    }

    public AgriculturalOperationResponse toResponse(AgriculturalOperation op) {
        return new AgriculturalOperationResponse(op.getId(), op.getTenantId(), op.getSeasonId(), op.getFieldId(), op.getTipo(), op.getStatus(),
                op.getDataOperacao(), op.getAreaExecutada(), op.getCustoEstimado(), op.getCustoReal(), op.getDescricao(),
                op.getCreatedAt(), op.getUpdatedAt(), op.getCreatedBy(), op.getUpdatedBy());
    }
}

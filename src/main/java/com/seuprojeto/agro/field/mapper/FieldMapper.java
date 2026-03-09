package com.seuprojeto.agro.field.mapper;

import com.seuprojeto.agro.field.domain.Field;
import com.seuprojeto.agro.field.dto.FieldCreateRequest;
import com.seuprojeto.agro.field.dto.FieldResponse;
import com.seuprojeto.agro.field.dto.FieldUpdateRequest;
import org.springframework.stereotype.Component;

@Component
public class FieldMapper {

    public Field toEntity(FieldCreateRequest request) {
        Field field = new Field();
        apply(field, request.farmId(), request.nome(), request.codigo(), request.areaHectares(), request.tipoSolo(),
                request.geoJson(), request.observacoes());
        return field;
    }

    public void updateEntity(Field field, FieldUpdateRequest request) {
        apply(field, request.farmId(), request.nome(), request.codigo(), request.areaHectares(), request.tipoSolo(),
                request.geoJson(), request.observacoes());
    }

    private void apply(Field field, java.util.UUID farmId, String nome, String codigo, java.math.BigDecimal areaHectares,
                       String tipoSolo, String geoJson, String observacoes) {
        field.setFarmId(farmId);
        field.setNome(nome);
        field.setCodigo(codigo);
        field.setAreaHectares(areaHectares);
        field.setTipoSolo(tipoSolo);
        field.setGeoJson(geoJson);
        field.setObservacoes(observacoes);
    }

    public FieldResponse toResponse(Field field) {
        return new FieldResponse(field.getId(), field.getTenantId(), field.getFarmId(), field.getNome(), field.getCodigo(),
                field.getAreaHectares(), field.getTipoSolo(), field.getStatus(), field.getGeoJson(), field.getObservacoes(),
                field.getCreatedAt(), field.getUpdatedAt(), field.getCreatedBy(), field.getUpdatedBy());
    }
}

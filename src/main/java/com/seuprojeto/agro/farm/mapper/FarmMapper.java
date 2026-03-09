package com.seuprojeto.agro.farm.mapper;

import com.seuprojeto.agro.farm.domain.Farm;
import com.seuprojeto.agro.farm.dto.FarmCreateRequest;
import com.seuprojeto.agro.farm.dto.FarmResponse;
import com.seuprojeto.agro.farm.dto.FarmUpdateRequest;
import org.springframework.stereotype.Component;

@Component
public class FarmMapper {

    public Farm toEntity(FarmCreateRequest request) {
        Farm farm = new Farm();
        apply(farm, request.nome(), request.proprietario(), request.areaTotalHectares(), request.cidade(), request.estado(),
                request.latitude(), request.longitude(), request.observacoes());
        return farm;
    }

    public void updateEntity(Farm farm, FarmUpdateRequest request) {
        apply(farm, request.nome(), request.proprietario(), request.areaTotalHectares(), request.cidade(), request.estado(),
                request.latitude(), request.longitude(), request.observacoes());
    }

    private void apply(Farm farm, String nome, String proprietario, java.math.BigDecimal areaTotalHectares, String cidade,
                       String estado, java.math.BigDecimal latitude, java.math.BigDecimal longitude, String observacoes) {
        farm.setNome(nome);
        farm.setProprietario(proprietario);
        farm.setAreaTotalHectares(areaTotalHectares);
        farm.setCidade(cidade);
        farm.setEstado(estado);
        farm.setLatitude(latitude);
        farm.setLongitude(longitude);
        farm.setObservacoes(observacoes);
    }

    public FarmResponse toResponse(Farm farm) {
        return new FarmResponse(farm.getId(), farm.getTenantId(), farm.getNome(), farm.getProprietario(), farm.getAreaTotalHectares(),
                farm.getCidade(), farm.getEstado(), farm.getLatitude(), farm.getLongitude(), farm.getObservacoes(), farm.getStatus(),
                farm.getCreatedAt(), farm.getUpdatedAt(), farm.getCreatedBy(), farm.getUpdatedBy());
    }
}

package com.seuprojeto.agro.season.mapper;

import com.seuprojeto.agro.season.domain.Season;
import com.seuprojeto.agro.season.dto.SeasonCreateRequest;
import com.seuprojeto.agro.season.dto.SeasonResponse;
import com.seuprojeto.agro.season.dto.SeasonUpdateRequest;
import org.springframework.stereotype.Component;

@Component
public class SeasonMapper {

    public Season toEntity(SeasonCreateRequest request) {
        Season season = new Season();
        apply(season, request.fieldId(), request.cropId(), request.nome(), request.anoSafra(), request.dataInicio(), request.dataFim(),
                request.areaPlantada(), request.previsaoProducao(), request.producaoReal(), request.observacoes());
        return season;
    }

    public void updateEntity(Season season, SeasonUpdateRequest request) {
        apply(season, request.fieldId(), request.cropId(), request.nome(), request.anoSafra(), request.dataInicio(), request.dataFim(),
                request.areaPlantada(), request.previsaoProducao(), request.producaoReal(), request.observacoes());
    }

    private void apply(Season season, java.util.UUID fieldId, java.util.UUID cropId, String nome, Integer anoSafra,
                       java.time.LocalDate dataInicio, java.time.LocalDate dataFim, java.math.BigDecimal areaPlantada,
                       java.math.BigDecimal previsaoProducao, java.math.BigDecimal producaoReal, String observacoes) {
        season.setFieldId(fieldId);
        season.setCropId(cropId);
        season.setNome(nome);
        season.setAnoSafra(anoSafra);
        season.setDataInicio(dataInicio);
        season.setDataFim(dataFim);
        season.setAreaPlantada(areaPlantada);
        season.setPrevisaoProducao(previsaoProducao);
        season.setProducaoReal(producaoReal);
        season.setObservacoes(observacoes);
    }

    public SeasonResponse toResponse(Season season) {
        return new SeasonResponse(season.getId(), season.getTenantId(), season.getFieldId(), season.getCropId(), season.getNome(),
                season.getAnoSafra(), season.getDataInicio(), season.getDataFim(), season.getAreaPlantada(), season.getStatus(),
                season.getPrevisaoProducao(), season.getProducaoReal(), season.getObservacoes(), season.getCreatedAt(),
                season.getUpdatedAt(), season.getCreatedBy(), season.getUpdatedBy());
    }
}

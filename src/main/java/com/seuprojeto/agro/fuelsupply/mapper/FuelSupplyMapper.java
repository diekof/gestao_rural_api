package com.seuprojeto.agro.fuelsupply.mapper;

import com.seuprojeto.agro.fuelsupply.domain.FuelSupply;
import com.seuprojeto.agro.fuelsupply.dto.FuelSupplyCreateRequest;
import com.seuprojeto.agro.fuelsupply.dto.FuelSupplyResponse;
import com.seuprojeto.agro.fuelsupply.dto.FuelSupplyUpdateRequest;
import java.time.Instant;
import java.time.OffsetDateTime;
import org.springframework.stereotype.Component;

@Component
public class FuelSupplyMapper {

    public FuelSupply toEntity(FuelSupplyCreateRequest request) {
        FuelSupply entity = new FuelSupply();
        entity.setMachineId(request.machineId());
        entity.setValor(request.valor());
        entity.setLitros(request.litros());
        entity.setAbastecidoEm(request.abastecidoEm());
        entity.setLocalizacao(request.localizacao());
        entity.setObservacao(request.observacao());
        return entity;
    }

    public void updateEntity(FuelSupply entity, FuelSupplyUpdateRequest request) {
        entity.setMachineId(request.machineId());
        entity.setValor(request.valor());
        entity.setLitros(request.litros());
        entity.setAbastecidoEm(request.abastecidoEm());
        entity.setLocalizacao(request.localizacao());
        entity.setObservacao(request.observacao());
    }

    public FuelSupplyResponse toResponse(FuelSupply entity) {
        return new FuelSupplyResponse(
                entity.getId(),
                entity.getTenantId(),
                entity.getCreditId(),
                entity.getUserId(),
                entity.getWorkerName(),
                entity.getMachineId(),
                entity.getMachineName(),
                entity.getLitros(),
                entity.getValor(),
                toInstant(entity.getAbastecidoEm()),
                entity.getLocalizacao(),
                entity.getObservacao(),
                toInstant(entity.getCreatedAt()),
                toInstant(entity.getUpdatedAt()),
                entity.getCreatedBy(),
                entity.getUpdatedBy()
        );
    }

    private Instant toInstant(OffsetDateTime value) {
        return value != null ? value.toInstant() : null;
    }
}


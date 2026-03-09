package com.seuprojeto.agro.machinerecord.mapper;

import com.seuprojeto.agro.machinerecord.domain.MachineRecord;
import com.seuprojeto.agro.machinerecord.dto.MachineRecordCreateRequest;
import com.seuprojeto.agro.machinerecord.dto.MachineRecordResponse;
import com.seuprojeto.agro.machinerecord.dto.MachineRecordUpdateRequest;
import org.springframework.stereotype.Component;

@Component
public class MachineRecordMapper {

    public MachineRecord toEntity(MachineRecordCreateRequest request) {
        MachineRecord entity = new MachineRecord();
        apply(entity, request.machineId(), request.operationId(), request.tipo(), request.dataRegistro(), request.horasTrabalhadas(), request.valor(), request.descricao());
        return entity;
    }

    public void updateEntity(MachineRecord entity, MachineRecordUpdateRequest request) {
        apply(entity, request.machineId(), request.operationId(), request.tipo(), request.dataRegistro(), request.horasTrabalhadas(), request.valor(), request.descricao());
    }

    private void apply(MachineRecord entity, java.util.UUID machineId, java.util.UUID operationId,
                       com.seuprojeto.agro.machinerecord.domain.MachineRecordType tipo, java.time.LocalDate dataRegistro,
                       java.math.BigDecimal horasTrabalhadas, java.math.BigDecimal valor, String descricao) {
        entity.setMachineId(machineId);
        entity.setOperationId(operationId);
        entity.setTipo(tipo);
        entity.setDataRegistro(dataRegistro);
        entity.setHorasTrabalhadas(horasTrabalhadas);
        entity.setValor(valor);
        entity.setDescricao(descricao);
    }

    public MachineRecordResponse toResponse(MachineRecord entity) {
        return new MachineRecordResponse(entity.getId(), entity.getTenantId(), entity.getMachineId(), entity.getOperationId(), entity.getTipo(),
                entity.getDataRegistro(), entity.getHorasTrabalhadas(), entity.getValor(), entity.getDescricao(),
                entity.getCreatedAt(), entity.getUpdatedAt(), entity.getCreatedBy(), entity.getUpdatedBy());
    }
}

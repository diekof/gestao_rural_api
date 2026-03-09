package com.seuprojeto.agro.machine.mapper;

import com.seuprojeto.agro.machine.domain.Machine;
import com.seuprojeto.agro.machine.dto.MachineCreateRequest;
import com.seuprojeto.agro.machine.dto.MachineResponse;
import com.seuprojeto.agro.machine.dto.MachineUpdateRequest;
import org.springframework.stereotype.Component;

@Component
public class MachineMapper {

    public Machine toEntity(MachineCreateRequest request) {
        Machine m = new Machine();
        m.setCodigo(request.codigo());
        m.setNome(request.nome());
        m.setTipo(request.tipo());
        m.setFabricante(request.fabricante());
        m.setModelo(request.modelo());
        m.setAnoFabricacao(request.anoFabricacao());
        m.setStatus(request.status());
        m.setHorimetroAtual(request.horimetroAtual());
        m.setObservacoes(request.observacoes());
        return m;
    }

    public void updateEntity(Machine machine, MachineUpdateRequest request) {
        machine.setCodigo(request.codigo());
        machine.setNome(request.nome());
        machine.setTipo(request.tipo());
        machine.setFabricante(request.fabricante());
        machine.setModelo(request.modelo());
        machine.setAnoFabricacao(request.anoFabricacao());
        machine.setStatus(request.status());
        machine.setHorimetroAtual(request.horimetroAtual());
        machine.setObservacoes(request.observacoes());
    }

    public MachineResponse toResponse(Machine m) {
        return new MachineResponse(m.getId(), m.getTenantId(), m.getCodigo(), m.getNome(), m.getTipo(), m.getFabricante(),
                m.getModelo(), m.getAnoFabricacao(), m.getStatus(), m.getHorimetroAtual(), m.getObservacoes(),
                m.getCreatedAt(), m.getUpdatedAt(), m.getCreatedBy(), m.getUpdatedBy());
    }
}

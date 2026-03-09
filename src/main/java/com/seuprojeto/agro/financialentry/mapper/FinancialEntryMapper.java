package com.seuprojeto.agro.financialentry.mapper;

import com.seuprojeto.agro.financialentry.domain.FinancialEntry;
import com.seuprojeto.agro.financialentry.dto.FinancialEntryCreateRequest;
import com.seuprojeto.agro.financialentry.dto.FinancialEntryResponse;
import com.seuprojeto.agro.financialentry.dto.FinancialEntryUpdateRequest;
import org.springframework.stereotype.Component;

@Component
public class FinancialEntryMapper {

    public FinancialEntry toEntity(FinancialEntryCreateRequest request) {
        FinancialEntry entry = new FinancialEntry();
        apply(entry, request.seasonId(), request.operationId(), request.tipo(), request.categoria(), request.status(),
                request.dataLancamento(), request.dataVencimento(), request.dataPagamento(), request.valor(), request.descricao(), request.documento());
        return entry;
    }

    public void updateEntity(FinancialEntry entry, FinancialEntryUpdateRequest request) {
        apply(entry, request.seasonId(), request.operationId(), request.tipo(), request.categoria(), request.status(),
                request.dataLancamento(), request.dataVencimento(), request.dataPagamento(), request.valor(), request.descricao(), request.documento());
    }

    private void apply(FinancialEntry entry, java.util.UUID seasonId, java.util.UUID operationId,
                       com.seuprojeto.agro.financialentry.domain.FinancialEntryType tipo,
                       com.seuprojeto.agro.financialentry.domain.FinancialCategory categoria,
                       com.seuprojeto.agro.financialentry.domain.FinancialEntryStatus status,
                       java.time.LocalDate dataLancamento, java.time.LocalDate dataVencimento, java.time.LocalDate dataPagamento,
                       java.math.BigDecimal valor, String descricao, String documento) {
        entry.setSeasonId(seasonId);
        entry.setOperationId(operationId);
        entry.setTipo(tipo);
        entry.setCategoria(categoria);
        entry.setStatus(status);
        entry.setDataLancamento(dataLancamento);
        entry.setDataVencimento(dataVencimento);
        entry.setDataPagamento(dataPagamento);
        entry.setValor(valor);
        entry.setDescricao(descricao);
        entry.setDocumento(documento);
    }

    public FinancialEntryResponse toResponse(FinancialEntry entry) {
        return new FinancialEntryResponse(entry.getId(), entry.getTenantId(), entry.getSeasonId(), entry.getOperationId(), entry.getTipo(),
                entry.getCategoria(), entry.getStatus(), entry.getDataLancamento(), entry.getDataVencimento(), entry.getDataPagamento(),
                entry.getValor(), entry.getDescricao(), entry.getDocumento(), entry.getCreatedAt(), entry.getUpdatedAt(),
                entry.getCreatedBy(), entry.getUpdatedBy());
    }
}

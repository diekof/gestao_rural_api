package com.seuprojeto.agro.financialentry.domain;

import com.seuprojeto.agro.audit.AuditableEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "financial_entries")
public class FinancialEntry extends AuditableEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID tenantId;

    @Column
    private UUID seasonId;

    @Column
    private UUID operationId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private FinancialEntryType tipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private FinancialCategory categoria;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private FinancialEntryStatus status;

    @Column(nullable = false)
    private LocalDate dataLancamento;

    @Column
    private LocalDate dataVencimento;

    @Column
    private LocalDate dataPagamento;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal valor;

    @Column(nullable = false, length = 180)
    private String descricao;

    @Column(length = 120)
    private String documento;

    @PrePersist
    public void prePersist() {
        if (id == null) id = UUID.randomUUID();
        if (status == null) status = FinancialEntryStatus.PENDING;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getTenantId() { return tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }
    public UUID getSeasonId() { return seasonId; }
    public void setSeasonId(UUID seasonId) { this.seasonId = seasonId; }
    public UUID getOperationId() { return operationId; }
    public void setOperationId(UUID operationId) { this.operationId = operationId; }
    public FinancialEntryType getTipo() { return tipo; }
    public void setTipo(FinancialEntryType tipo) { this.tipo = tipo; }
    public FinancialCategory getCategoria() { return categoria; }
    public void setCategoria(FinancialCategory categoria) { this.categoria = categoria; }
    public FinancialEntryStatus getStatus() { return status; }
    public void setStatus(FinancialEntryStatus status) { this.status = status; }
    public LocalDate getDataLancamento() { return dataLancamento; }
    public void setDataLancamento(LocalDate dataLancamento) { this.dataLancamento = dataLancamento; }
    public LocalDate getDataVencimento() { return dataVencimento; }
    public void setDataVencimento(LocalDate dataVencimento) { this.dataVencimento = dataVencimento; }
    public LocalDate getDataPagamento() { return dataPagamento; }
    public void setDataPagamento(LocalDate dataPagamento) { this.dataPagamento = dataPagamento; }
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }
}

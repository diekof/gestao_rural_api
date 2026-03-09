package com.seuprojeto.agro.agriculturaloperation.domain;

import com.seuprojeto.agro.audit.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "agricultural_operations")
public class AgriculturalOperation extends AuditableEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID tenantId;

    @Column(nullable = false)
    private UUID seasonId;

    @Column(nullable = false)
    private UUID fieldId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private OperationType tipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OperationStatus status;

    @Column(nullable = false)
    private LocalDate dataOperacao;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal areaExecutada;

    @Column(precision = 14, scale = 2)
    private BigDecimal custoEstimado;

    @Column(precision = 14, scale = 2)
    private BigDecimal custoReal;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @PrePersist
    public void prePersist() {
        if (id == null) id = UUID.randomUUID();
        if (status == null) status = OperationStatus.PLANNED;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getTenantId() { return tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }
    public UUID getSeasonId() { return seasonId; }
    public void setSeasonId(UUID seasonId) { this.seasonId = seasonId; }
    public UUID getFieldId() { return fieldId; }
    public void setFieldId(UUID fieldId) { this.fieldId = fieldId; }
    public OperationType getTipo() { return tipo; }
    public void setTipo(OperationType tipo) { this.tipo = tipo; }
    public OperationStatus getStatus() { return status; }
    public void setStatus(OperationStatus status) { this.status = status; }
    public LocalDate getDataOperacao() { return dataOperacao; }
    public void setDataOperacao(LocalDate dataOperacao) { this.dataOperacao = dataOperacao; }
    public BigDecimal getAreaExecutada() { return areaExecutada; }
    public void setAreaExecutada(BigDecimal areaExecutada) { this.areaExecutada = areaExecutada; }
    public BigDecimal getCustoEstimado() { return custoEstimado; }
    public void setCustoEstimado(BigDecimal custoEstimado) { this.custoEstimado = custoEstimado; }
    public BigDecimal getCustoReal() { return custoReal; }
    public void setCustoReal(BigDecimal custoReal) { this.custoReal = custoReal; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
}

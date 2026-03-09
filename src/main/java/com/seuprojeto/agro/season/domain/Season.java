package com.seuprojeto.agro.season.domain;

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
@Table(name = "seasons")
public class Season extends AuditableEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID tenantId;

    @Column(nullable = false)
    private UUID fieldId;

    @Column(nullable = false)
    private UUID cropId;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false)
    private Integer anoSafra;

    @Column(nullable = false)
    private LocalDate dataInicio;

    @Column
    private LocalDate dataFim;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal areaPlantada;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SeasonStatus status;

    @Column(precision = 12, scale = 2)
    private BigDecimal previsaoProducao;

    @Column(precision = 12, scale = 2)
    private BigDecimal producaoReal;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @PrePersist
    public void prePersist() {
        if (id == null) id = UUID.randomUUID();
        if (status == null) status = SeasonStatus.PLANNED;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getTenantId() { return tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }
    public UUID getFieldId() { return fieldId; }
    public void setFieldId(UUID fieldId) { this.fieldId = fieldId; }
    public UUID getCropId() { return cropId; }
    public void setCropId(UUID cropId) { this.cropId = cropId; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public Integer getAnoSafra() { return anoSafra; }
    public void setAnoSafra(Integer anoSafra) { this.anoSafra = anoSafra; }
    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }
    public LocalDate getDataFim() { return dataFim; }
    public void setDataFim(LocalDate dataFim) { this.dataFim = dataFim; }
    public BigDecimal getAreaPlantada() { return areaPlantada; }
    public void setAreaPlantada(BigDecimal areaPlantada) { this.areaPlantada = areaPlantada; }
    public SeasonStatus getStatus() { return status; }
    public void setStatus(SeasonStatus status) { this.status = status; }
    public BigDecimal getPrevisaoProducao() { return previsaoProducao; }
    public void setPrevisaoProducao(BigDecimal previsaoProducao) { this.previsaoProducao = previsaoProducao; }
    public BigDecimal getProducaoReal() { return producaoReal; }
    public void setProducaoReal(BigDecimal producaoReal) { this.producaoReal = producaoReal; }
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
}

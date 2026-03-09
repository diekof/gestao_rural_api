package com.seuprojeto.agro.crop.domain;

import com.seuprojeto.agro.audit.AuditableEntity;
import com.seuprojeto.agro.common.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "crops")
public class Crop extends AuditableEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID tenantId;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(length = 80)
    private String categoria;

    @Column(nullable = false)
    private Integer cicloMedioDias;

    @Column(precision = 12, scale = 2)
    private BigDecimal produtividadeEsperada;

    @Column(length = 30)
    private String unidadeProdutividade;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status;

    @PrePersist
    public void prePersist() {
        if (id == null) id = UUID.randomUUID();
        if (status == null) status = Status.ACTIVE;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getTenantId() { return tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public Integer getCicloMedioDias() { return cicloMedioDias; }
    public void setCicloMedioDias(Integer cicloMedioDias) { this.cicloMedioDias = cicloMedioDias; }
    public BigDecimal getProdutividadeEsperada() { return produtividadeEsperada; }
    public void setProdutividadeEsperada(BigDecimal produtividadeEsperada) { this.produtividadeEsperada = produtividadeEsperada; }
    public String getUnidadeProdutividade() { return unidadeProdutividade; }
    public void setUnidadeProdutividade(String unidadeProdutividade) { this.unidadeProdutividade = unidadeProdutividade; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
}

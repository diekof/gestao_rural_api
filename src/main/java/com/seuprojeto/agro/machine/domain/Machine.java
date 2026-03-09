package com.seuprojeto.agro.machine.domain;

import com.seuprojeto.agro.audit.AuditableEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "machines")
public class Machine extends AuditableEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID tenantId;

    @Column(nullable = false, length = 60)
    private String codigo;

    @Column(nullable = false, length = 140)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private MachineType tipo;

    @Column(length = 100)
    private String fabricante;

    @Column(length = 100)
    private String modelo;

    @Column
    private Integer anoFabricacao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MachineStatus status;

    @Column(precision = 12, scale = 2)
    private BigDecimal horimetroAtual;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @PrePersist
    public void prePersist() {
        if (id == null) id = UUID.randomUUID();
        if (status == null) status = MachineStatus.ACTIVE;
        if (horimetroAtual == null) horimetroAtual = BigDecimal.ZERO;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getTenantId() { return tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public MachineType getTipo() { return tipo; }
    public void setTipo(MachineType tipo) { this.tipo = tipo; }
    public String getFabricante() { return fabricante; }
    public void setFabricante(String fabricante) { this.fabricante = fabricante; }
    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    public Integer getAnoFabricacao() { return anoFabricacao; }
    public void setAnoFabricacao(Integer anoFabricacao) { this.anoFabricacao = anoFabricacao; }
    public MachineStatus getStatus() { return status; }
    public void setStatus(MachineStatus status) { this.status = status; }
    public BigDecimal getHorimetroAtual() { return horimetroAtual; }
    public void setHorimetroAtual(BigDecimal horimetroAtual) { this.horimetroAtual = horimetroAtual; }
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
}

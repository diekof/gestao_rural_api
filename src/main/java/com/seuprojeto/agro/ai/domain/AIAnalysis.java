package com.seuprojeto.agro.ai.domain;

import com.seuprojeto.agro.audit.AuditableEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ai_analyses")
public class AIAnalysis extends AuditableEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID tenantId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private AIAnalysisType tipo;

    @Column
    private UUID seasonId;

    @Column
    private UUID fieldId;

    @Column(precision = 8, scale = 2)
    private BigDecimal score;

    @Column(nullable = false, length = 180)
    private String resumo;

    @Column(columnDefinition = "TEXT")
    private String recomendacoes;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String inputData;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String resultData;

    @Column(nullable = false)
    private LocalDateTime analysisDate;

    @PrePersist
    public void prePersist() {
        if (id == null) id = UUID.randomUUID();
        if (analysisDate == null) analysisDate = LocalDateTime.now();
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getTenantId() { return tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }
    public AIAnalysisType getTipo() { return tipo; }
    public void setTipo(AIAnalysisType tipo) { this.tipo = tipo; }
    public UUID getSeasonId() { return seasonId; }
    public void setSeasonId(UUID seasonId) { this.seasonId = seasonId; }
    public UUID getFieldId() { return fieldId; }
    public void setFieldId(UUID fieldId) { this.fieldId = fieldId; }
    public BigDecimal getScore() { return score; }
    public void setScore(BigDecimal score) { this.score = score; }
    public String getResumo() { return resumo; }
    public void setResumo(String resumo) { this.resumo = resumo; }
    public String getRecomendacoes() { return recomendacoes; }
    public void setRecomendacoes(String recomendacoes) { this.recomendacoes = recomendacoes; }
    public String getInputData() { return inputData; }
    public void setInputData(String inputData) { this.inputData = inputData; }
    public String getResultData() { return resultData; }
    public void setResultData(String resultData) { this.resultData = resultData; }
    public LocalDateTime getAnalysisDate() { return analysisDate; }
    public void setAnalysisDate(LocalDateTime analysisDate) { this.analysisDate = analysisDate; }
}

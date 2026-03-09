package com.seuprojeto.agro.satellite.domain;

import com.seuprojeto.agro.audit.AuditableEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "satellite_images")
public class SatelliteImage extends AuditableEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID tenantId;

    @Column(nullable = false)
    private UUID fieldId;

    @Column(nullable = false)
    private LocalDateTime capturedAt;

    @Column(nullable = false, length = 80)
    private String provider;

    @Column(nullable = false, length = 300)
    private String imageUrl;

    @Column(precision = 5, scale = 2)
    private BigDecimal cloudCoverage;

    @Column(precision = 6, scale = 4)
    private BigDecimal ndviAverage;

    @Column(columnDefinition = "TEXT")
    private String metadata;

    @PrePersist
    public void prePersist() {
        if (id == null) id = UUID.randomUUID();
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getTenantId() { return tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }
    public UUID getFieldId() { return fieldId; }
    public void setFieldId(UUID fieldId) { this.fieldId = fieldId; }
    public LocalDateTime getCapturedAt() { return capturedAt; }
    public void setCapturedAt(LocalDateTime capturedAt) { this.capturedAt = capturedAt; }
    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public BigDecimal getCloudCoverage() { return cloudCoverage; }
    public void setCloudCoverage(BigDecimal cloudCoverage) { this.cloudCoverage = cloudCoverage; }
    public BigDecimal getNdviAverage() { return ndviAverage; }
    public void setNdviAverage(BigDecimal ndviAverage) { this.ndviAverage = ndviAverage; }
    public String getMetadata() { return metadata; }
    public void setMetadata(String metadata) { this.metadata = metadata; }
}

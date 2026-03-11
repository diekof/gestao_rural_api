package com.seuprojeto.agro.fuelcredit.domain;

import com.seuprojeto.agro.audit.AuditableEntity;
import com.seuprojeto.agro.user.domain.User;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "fuel_credits", uniqueConstraints = {
        @UniqueConstraint(name = "uk_fuel_credit_tenant_user", columnNames = {"tenant_id", "user_id"})
})
public class FuelCredit extends AuditableEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID tenantId;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal creditLimit;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private FuelCreditStatus status;

    @Column
    private OffsetDateTime lastRechargeAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @PrePersist
    public void prePersist() {
        if (id == null) id = UUID.randomUUID();
        if (status == null) status = FuelCreditStatus.ACTIVE;
        if (balance == null) balance = BigDecimal.ZERO;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getTenantId() { return tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }
    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
    public BigDecimal getCreditLimit() { return creditLimit; }
    public void setCreditLimit(BigDecimal creditLimit) { this.creditLimit = creditLimit; }
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
    public FuelCreditStatus getStatus() { return status; }
    public void setStatus(FuelCreditStatus status) { this.status = status; }
    public OffsetDateTime getLastRechargeAt() { return lastRechargeAt; }
    public void setLastRechargeAt(OffsetDateTime lastRechargeAt) { this.lastRechargeAt = lastRechargeAt; }
    public User getUser() { return user; }
}


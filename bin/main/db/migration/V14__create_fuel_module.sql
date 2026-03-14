CREATE TABLE fuel_credits (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL REFERENCES tenants(id),
    user_id UUID NOT NULL REFERENCES users(id),
    credit_limit NUMERIC(14,2) NOT NULL,
    balance NUMERIC(14,2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    last_recharge_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    created_by VARCHAR(100) NOT NULL,
    updated_by VARCHAR(100) NOT NULL,
    CONSTRAINT chk_fuel_credit_limit CHECK (credit_limit >= 0),
    CONSTRAINT chk_fuel_credit_balance CHECK (balance >= 0),
    CONSTRAINT chk_fuel_credit_balance_limit CHECK (balance <= credit_limit)
);

CREATE UNIQUE INDEX uk_fuel_credit_tenant_user ON fuel_credits(tenant_id, user_id);
CREATE INDEX idx_fuel_credit_tenant ON fuel_credits(tenant_id);

CREATE TABLE fuel_supplies (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL REFERENCES tenants(id),
    credit_id UUID NOT NULL REFERENCES fuel_credits(id),
    user_id UUID NOT NULL REFERENCES users(id),
    machine_id UUID REFERENCES machines(id),
    worker_name VARCHAR(140) NOT NULL,
    machine_name VARCHAR(140),
    abastecido_em TIMESTAMP WITH TIME ZONE NOT NULL,
    litros NUMERIC(12,2) NOT NULL,
    valor NUMERIC(14,2) NOT NULL,
    localizacao VARCHAR(160),
    observacao TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    created_by VARCHAR(100) NOT NULL,
    updated_by VARCHAR(100) NOT NULL
);

CREATE INDEX idx_fuel_supply_tenant ON fuel_supplies(tenant_id);
CREATE INDEX idx_fuel_supply_user ON fuel_supplies(user_id);
CREATE INDEX idx_fuel_supply_machine ON fuel_supplies(machine_id);
CREATE INDEX idx_fuel_supply_date ON fuel_supplies(abastecido_em);


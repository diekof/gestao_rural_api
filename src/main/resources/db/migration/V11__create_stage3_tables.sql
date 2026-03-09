CREATE TABLE agricultural_operations (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL REFERENCES tenants(id),
    season_id UUID NOT NULL REFERENCES seasons(id),
    field_id UUID NOT NULL REFERENCES fields(id),
    tipo VARCHAR(30) NOT NULL,
    status VARCHAR(20) NOT NULL,
    data_operacao DATE NOT NULL,
    area_executada NUMERIC(12,2) NOT NULL,
    custo_estimado NUMERIC(14,2),
    custo_real NUMERIC(14,2),
    descricao TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    created_by VARCHAR(100) NOT NULL,
    updated_by VARCHAR(100) NOT NULL,
    CONSTRAINT uk_agri_operation_tenant_season_date_type UNIQUE (tenant_id, season_id, data_operacao, tipo)
);

CREATE TABLE machines (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL REFERENCES tenants(id),
    codigo VARCHAR(60) NOT NULL,
    nome VARCHAR(140) NOT NULL,
    tipo VARCHAR(30) NOT NULL,
    fabricante VARCHAR(100),
    modelo VARCHAR(100),
    ano_fabricacao INTEGER,
    status VARCHAR(20) NOT NULL,
    horimetro_atual NUMERIC(12,2),
    observacoes TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    created_by VARCHAR(100) NOT NULL,
    updated_by VARCHAR(100) NOT NULL,
    CONSTRAINT uk_machine_tenant_code UNIQUE (tenant_id, codigo)
);

CREATE TABLE machine_records (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL REFERENCES tenants(id),
    machine_id UUID NOT NULL REFERENCES machines(id),
    operation_id UUID REFERENCES agricultural_operations(id),
    tipo VARCHAR(20) NOT NULL,
    data_registro DATE NOT NULL,
    horas_trabalhadas NUMERIC(10,2),
    valor NUMERIC(14,2),
    descricao TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    created_by VARCHAR(100) NOT NULL,
    updated_by VARCHAR(100) NOT NULL
);

CREATE TABLE financial_entries (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL REFERENCES tenants(id),
    season_id UUID REFERENCES seasons(id),
    operation_id UUID REFERENCES agricultural_operations(id),
    tipo VARCHAR(20) NOT NULL,
    categoria VARCHAR(30) NOT NULL,
    status VARCHAR(20) NOT NULL,
    data_lancamento DATE NOT NULL,
    data_vencimento DATE,
    data_pagamento DATE,
    valor NUMERIC(14,2) NOT NULL,
    descricao VARCHAR(180) NOT NULL,
    documento VARCHAR(120),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    created_by VARCHAR(100) NOT NULL,
    updated_by VARCHAR(100) NOT NULL
);

CREATE INDEX idx_agri_operations_tenant ON agricultural_operations(tenant_id);
CREATE INDEX idx_agri_operations_season ON agricultural_operations(season_id);
CREATE INDEX idx_machines_tenant ON machines(tenant_id);
CREATE INDEX idx_machine_records_tenant ON machine_records(tenant_id);
CREATE INDEX idx_machine_records_machine ON machine_records(machine_id);
CREATE INDEX idx_financial_entries_tenant ON financial_entries(tenant_id);
CREATE INDEX idx_financial_entries_operation ON financial_entries(operation_id);

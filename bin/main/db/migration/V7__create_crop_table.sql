CREATE TABLE crops (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL,
    nome VARCHAR(120) NOT NULL,
    categoria VARCHAR(80),
    ciclo_medio_dias INTEGER NOT NULL,
    produtividade_esperada NUMERIC(12,2),
    unidade_produtividade VARCHAR(30),
    descricao TEXT,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    created_by VARCHAR(100) NOT NULL,
    updated_by VARCHAR(100) NOT NULL,
    CONSTRAINT fk_crops_tenant FOREIGN KEY (tenant_id) REFERENCES tenants(id),
    CONSTRAINT ck_crops_ciclo_min CHECK (ciclo_medio_dias >= 1),
    CONSTRAINT ck_crops_produtividade_positive CHECK (produtividade_esperada IS NULL OR produtividade_esperada > 0)
);

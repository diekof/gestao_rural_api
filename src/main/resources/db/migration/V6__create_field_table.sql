CREATE TABLE fields (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL,
    farm_id UUID NOT NULL,
    nome VARCHAR(150) NOT NULL,
    codigo VARCHAR(50),
    area_hectares NUMERIC(12,2) NOT NULL,
    tipo_solo VARCHAR(80),
    status VARCHAR(20) NOT NULL,
    geo_json TEXT,
    observacoes TEXT,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    created_by VARCHAR(100) NOT NULL,
    updated_by VARCHAR(100) NOT NULL,
    CONSTRAINT fk_fields_tenant FOREIGN KEY (tenant_id) REFERENCES tenants(id),
    CONSTRAINT fk_fields_farm FOREIGN KEY (farm_id) REFERENCES farms(id),
    CONSTRAINT ck_fields_area_positive CHECK (area_hectares > 0)
);

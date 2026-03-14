CREATE TABLE ai_analyses (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL REFERENCES tenants(id),
    tipo VARCHAR(40) NOT NULL,
    season_id UUID REFERENCES seasons(id),
    field_id UUID REFERENCES fields(id),
    score NUMERIC(8,2),
    resumo VARCHAR(180) NOT NULL,
    recomendacoes TEXT,
    input_data TEXT NOT NULL,
    result_data TEXT NOT NULL,
    analysis_date TIMESTAMP NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    created_by VARCHAR(100) NOT NULL,
    updated_by VARCHAR(100) NOT NULL
);

CREATE TABLE satellite_images (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL REFERENCES tenants(id),
    field_id UUID NOT NULL REFERENCES fields(id),
    captured_at TIMESTAMP NOT NULL,
    provider VARCHAR(80) NOT NULL,
    image_url VARCHAR(300) NOT NULL,
    cloud_coverage NUMERIC(5,2),
    ndvi_average NUMERIC(6,4),
    metadata TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    created_by VARCHAR(100) NOT NULL,
    updated_by VARCHAR(100) NOT NULL
);

CREATE INDEX idx_ai_analyses_tenant ON ai_analyses(tenant_id);
CREATE INDEX idx_ai_analyses_type ON ai_analyses(tipo);
CREATE INDEX idx_satellite_images_tenant ON satellite_images(tenant_id);
CREATE INDEX idx_satellite_images_field_date ON satellite_images(field_id, captured_at);

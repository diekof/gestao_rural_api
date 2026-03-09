CREATE INDEX idx_farms_tenant ON farms(tenant_id);
CREATE INDEX idx_farms_tenant_nome ON farms(tenant_id, LOWER(nome));
CREATE INDEX idx_farms_tenant_status ON farms(tenant_id, status);
CREATE UNIQUE INDEX uk_farms_tenant_nome ON farms(tenant_id, LOWER(nome));

CREATE INDEX idx_fields_tenant ON fields(tenant_id);
CREATE INDEX idx_fields_farm ON fields(farm_id);
CREATE INDEX idx_fields_tenant_farm ON fields(tenant_id, farm_id);
CREATE INDEX idx_fields_tenant_nome ON fields(tenant_id, LOWER(nome));
CREATE INDEX idx_fields_tenant_status ON fields(tenant_id, status);
CREATE UNIQUE INDEX uk_fields_farm_codigo ON fields(farm_id, LOWER(codigo)) WHERE codigo IS NOT NULL;

CREATE INDEX idx_crops_tenant ON crops(tenant_id);
CREATE INDEX idx_crops_tenant_nome ON crops(tenant_id, LOWER(nome));
CREATE INDEX idx_crops_tenant_status ON crops(tenant_id, status);
CREATE UNIQUE INDEX uk_crops_tenant_nome ON crops(tenant_id, LOWER(nome));

CREATE INDEX idx_seasons_tenant ON seasons(tenant_id);
CREATE INDEX idx_seasons_field ON seasons(field_id);
CREATE INDEX idx_seasons_crop ON seasons(crop_id);
CREATE INDEX idx_seasons_tenant_status ON seasons(tenant_id, status);
CREATE INDEX idx_seasons_tenant_ano ON seasons(tenant_id, ano_safra);
CREATE UNIQUE INDEX uk_seasons_tenant_field_nome_ano ON seasons(tenant_id, field_id, LOWER(nome), ano_safra);

INSERT INTO farms (id, tenant_id, nome, proprietario, area_total_hectares, cidade, estado, latitude, longitude, observacoes, status, created_at, updated_at, created_by, updated_by)
VALUES
('21111111-1111-1111-1111-111111111111', '11111111-1111-1111-1111-111111111111', 'Fazenda Boa Esperança', 'João Silva', 1200.00, 'Rondonópolis', 'MT', -16.4719, -54.6350, 'Unidade principal', 'ACTIVE', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
('22222222-2222-2222-2222-222222222222', '11111111-1111-1111-1111-111111111111', 'Fazenda Santa Luzia', 'Maria Souza', 850.00, 'Sorriso', 'MT', -12.5420, -55.7210, 'Unidade secundária', 'ACTIVE', NOW(), NOW(), 'SYSTEM', 'SYSTEM');

INSERT INTO fields (id, tenant_id, farm_id, nome, codigo, area_hectares, tipo_solo, status, geo_json, observacoes, created_at, updated_at, created_by, updated_by)
VALUES
('31111111-1111-1111-1111-111111111111', '11111111-1111-1111-1111-111111111111', '21111111-1111-1111-1111-111111111111', 'Talhão Norte', 'TN-01', 400.00, 'ARGILOSO', 'ACTIVE', NULL, 'Área de alta produtividade', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
('32222222-2222-2222-2222-222222222222', '11111111-1111-1111-1111-111111111111', '21111111-1111-1111-1111-111111111111', 'Talhão Sul', 'TS-01', 350.00, 'MISTO', 'ACTIVE', NULL, 'Área irrigada', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
('33333333-3333-3333-3333-333333333333', '11111111-1111-1111-1111-111111111111', '22222222-2222-2222-2222-222222222222', 'Talhão Oeste', 'TO-01', 250.00, 'ARENOSO', 'ACTIVE', NULL, 'Área com rotação', NOW(), NOW(), 'SYSTEM', 'SYSTEM');

INSERT INTO crops (id, tenant_id, nome, categoria, ciclo_medio_dias, produtividade_esperada, unidade_produtividade, descricao, status, created_at, updated_at, created_by, updated_by)
VALUES
('41111111-1111-1111-1111-111111111111', '11111111-1111-1111-1111-111111111111', 'Soja', 'GRÃOS', 120, 62.00, 'sacas/ha', 'Cultivar precoce', 'ACTIVE', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
('42222222-2222-2222-2222-222222222222', '11111111-1111-1111-1111-111111111111', 'Milho', 'GRÃOS', 140, 180.00, 'sacas/ha', 'Segunda safra', 'ACTIVE', NOW(), NOW(), 'SYSTEM', 'SYSTEM');

INSERT INTO seasons (id, tenant_id, field_id, crop_id, nome, ano_safra, data_inicio, data_fim, area_plantada, status, previsao_producao, producao_real, observacoes, created_at, updated_at, created_by, updated_by)
VALUES
('51111111-1111-1111-1111-111111111111', '11111111-1111-1111-1111-111111111111', '31111111-1111-1111-1111-111111111111', '41111111-1111-1111-1111-111111111111', 'Safra Verão', 2025, DATE '2025-10-01', NULL, 380.00, 'PLANNED', 23560.00, NULL, 'Planejamento inicial', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
('52222222-2222-2222-2222-222222222222', '11111111-1111-1111-1111-111111111111', '32222222-2222-2222-2222-222222222222', '42222222-2222-2222-2222-222222222222', 'Safra Inverno', 2025, DATE '2025-02-15', DATE '2025-07-20', 300.00, 'HARVESTED', 54000.00, 52500.00, 'Fechada com rendimento abaixo do previsto', NOW(), NOW(), 'SYSTEM', 'SYSTEM');

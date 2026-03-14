INSERT INTO agricultural_operations (id, tenant_id, season_id, field_id, tipo, status, data_operacao, area_executada, custo_estimado, custo_real, descricao, created_at, updated_at, created_by, updated_by)
VALUES
('61111111-1111-1111-1111-111111111111', '11111111-1111-1111-1111-111111111111', '51111111-1111-1111-1111-111111111111', '31111111-1111-1111-1111-111111111111', 'PLANTING', 'COMPLETED', DATE '2025-10-05', 350.00, 42000.00, 41500.00, 'Plantio da soja com taxa variável', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
('62222222-2222-2222-2222-222222222222', '11111111-1111-1111-1111-111111111111', '52222222-2222-2222-2222-222222222222', '32222222-2222-2222-2222-222222222222', 'HARVEST', 'COMPLETED', DATE '2025-07-10', 300.00, 38000.00, 39500.00, 'Colheita do milho safrinha', NOW(), NOW(), 'SYSTEM', 'SYSTEM');

INSERT INTO machines (id, tenant_id, codigo, nome, tipo, fabricante, modelo, ano_fabricacao, status, horimetro_atual, observacoes, created_at, updated_at, created_by, updated_by)
VALUES
('71111111-1111-1111-1111-111111111111', '11111111-1111-1111-1111-111111111111', 'TR-001', 'Trator Magnum 340', 'TRACTOR', 'Case IH', 'Magnum 340', 2021, 'ACTIVE', 1850.50, 'Trator principal da frota', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
('72222222-2222-2222-2222-222222222222', '11111111-1111-1111-1111-111111111111', 'CL-001', 'Colheitadeira S780', 'HARVESTER', 'John Deere', 'S780', 2020, 'ACTIVE', 2400.00, 'Colheitadeira com piloto automático', NOW(), NOW(), 'SYSTEM', 'SYSTEM');

INSERT INTO machine_records (id, tenant_id, machine_id, operation_id, tipo, data_registro, horas_trabalhadas, valor, descricao, created_at, updated_at, created_by, updated_by)
VALUES
('81111111-1111-1111-1111-111111111111', '11111111-1111-1111-1111-111111111111', '71111111-1111-1111-1111-111111111111', '61111111-1111-1111-1111-111111111111', 'USAGE', DATE '2025-10-05', 12.50, 0.00, 'Uso em operação de plantio', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
('82222222-2222-2222-2222-222222222222', '11111111-1111-1111-1111-111111111111', '72222222-2222-2222-2222-222222222222', NULL, 'MAINTENANCE', DATE '2025-06-01', 0.00, 8600.00, 'Troca de componentes da plataforma', NOW(), NOW(), 'SYSTEM', 'SYSTEM');

INSERT INTO financial_entries (id, tenant_id, season_id, operation_id, tipo, categoria, status, data_lancamento, data_vencimento, data_pagamento, valor, descricao, documento, created_at, updated_at, created_by, updated_by)
VALUES
('91111111-1111-1111-1111-111111111111', '11111111-1111-1111-1111-111111111111', '51111111-1111-1111-1111-111111111111', '61111111-1111-1111-1111-111111111111', 'EXPENSE', 'INPUTS', 'PAID', DATE '2025-10-01', DATE '2025-10-10', DATE '2025-10-08', 125000.00, 'Compra de sementes e fertilizantes', 'NF-2025-0001', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
('92222222-2222-2222-2222-222222222222', '11111111-1111-1111-1111-111111111111', '52222222-2222-2222-2222-222222222222', '62222222-2222-2222-2222-222222222222', 'REVENUE', 'SALE', 'PENDING', DATE '2025-07-20', DATE '2025-08-20', NULL, 560000.00, 'Venda de milho para cooperativa', 'CTR-2025-078', NOW(), NOW(), 'SYSTEM', 'SYSTEM');

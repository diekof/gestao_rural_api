INSERT INTO users (id, tenant_id, nome, email, username, password_hash, role, status, created_at, updated_at, created_by, updated_by)
VALUES
('00000000-0000-0000-0000-000000000003', '11111111-1111-1111-1111-111111111111', 'Gerente Operacional', 'gerente@demoagro.local', 'gerentecampo', '$2a$10$LiYwKXu95syEHCqB6fINFuIh1BSrR2MvooA1Yj6M4f5XhVwHZfLhG', 'MANAGER', 'ACTIVE', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
('00000000-0000-0000-0000-000000000004', '11111111-1111-1111-1111-111111111111', 'JoÃ£o PeÃ£o', 'joao.peao@demoagro.local', 'joao.peao', '$2a$10$LiYwKXu95syEHCqB6fINFuIh1BSrR2MvooA1Yj6M4f5XhVwHZfLhG', 'OPERATOR', 'ACTIVE', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
('00000000-0000-0000-0000-000000000005', '11111111-1111-1111-1111-111111111111', 'Maria Campo', 'maria.campo@demoagro.local', 'maria.campo', '$2a$10$LiYwKXu95syEHCqB6fINFuIh1BSrR2MvooA1Yj6M4f5XhVwHZfLhG', 'OPERATOR', 'ACTIVE', NOW(), NOW(), 'SYSTEM', 'SYSTEM');

INSERT INTO fuel_credits (id, tenant_id, user_id, credit_limit, balance, status, last_recharge_at, created_at, updated_at, created_by, updated_by)
VALUES
('a1111111-1111-1111-1111-111111111111', '11111111-1111-1111-1111-111111111111', '00000000-0000-0000-0000-000000000004', 4500.00, 2200.00, 'ACTIVE', NOW() - INTERVAL '5 days', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
('a2222222-2222-2222-2222-222222222222', '11111111-1111-1111-1111-111111111111', '00000000-0000-0000-0000-000000000005', 3000.00, 2620.00, 'ACTIVE', NOW() - INTERVAL '2 days', NOW(), NOW(), 'SYSTEM', 'SYSTEM');

INSERT INTO fuel_supplies (id, tenant_id, credit_id, user_id, machine_id, worker_name, machine_name, abastecido_em, litros, valor, localizacao, observacao, created_at, updated_at, created_by, updated_by)
VALUES
('b1111111-1111-1111-1111-111111111111', '11111111-1111-1111-1111-111111111111', 'a1111111-1111-1111-1111-111111111111', '00000000-0000-0000-0000-000000000004', '71111111-1111-1111-1111-111111111111', 'JoÃ£o PeÃ£o', 'Trator Magnum 340', TIMESTAMPTZ '2025-10-03T09:40:00Z', 180.00, 1250.00, 'Posto interno 01', 'Completo antes do plantio', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
('b2222222-2222-2222-2222-222222222222', '11111111-1111-1111-1111-111111111111', 'a1111111-1111-1111-1111-111111111111', '00000000-0000-0000-0000-000000000004', '72222222-2222-2222-2222-222222222222', 'JoÃ£o PeÃ£o', 'Colheitadeira S780', TIMESTAMPTZ '2025-10-08T17:10:00Z', 220.00, 1050.00, 'Ponto de apoio 02', 'ReforÃ§o para colheita', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
('b3333333-3333-3333-3333-333333333333', '11111111-1111-1111-1111-111111111111', 'a2222222-2222-2222-2222-222222222222', '00000000-0000-0000-0000-000000000005', '71111111-1111-1111-1111-111111111111', 'Maria Campo', 'Trator Magnum 340', TIMESTAMPTZ '2025-09-18T06:50:00Z', 140.00, 380.00, 'Base oeste', 'Deslocamento entre talhÃµes', NOW(), NOW(), 'SYSTEM', 'SYSTEM');


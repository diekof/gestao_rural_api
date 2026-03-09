INSERT INTO tenants (id, nome, nome_fantasia, cpf_cnpj, email, telefone, plano, status, created_at, updated_at, created_by, updated_by)
VALUES ('11111111-1111-1111-1111-111111111111', 'Tenant Demo Agro', 'Demo Agro', '12345678000199', 'demo@agro.local', '11999999999', 'PRO', 'ACTIVE', NOW(), NOW(), 'SYSTEM', 'SYSTEM');

INSERT INTO users (id, tenant_id, nome, email, username, password_hash, role, status, created_at, updated_at, created_by, updated_by)
VALUES
('00000000-0000-0000-0000-000000000001', NULL, 'Super Admin', 'superadmin@agro.local', 'superadmin', '$2a$10$LiYwKXu95syEHCqB6fINFuIh1BSrR2MvooA1Yj6M4f5XhVwHZfLhG', 'SUPER_ADMIN', 'ACTIVE', NOW(), NOW(), 'SYSTEM', 'SYSTEM'),
('00000000-0000-0000-0000-000000000002', '11111111-1111-1111-1111-111111111111', 'Admin Demo', 'admin@demoagro.local', 'admindemo', '$2a$10$LiYwKXu95syEHCqB6fINFuIh1BSrR2MvooA1Yj6M4f5XhVwHZfLhG', 'TENANT_ADMIN', 'ACTIVE', NOW(), NOW(), 'SYSTEM', 'SYSTEM');

# Gestão Rural API - Etapa 1

Base SaaS multi-tenant com Java 21 + Spring Boot 3 para autenticação, tenants e usuários.

## Credenciais de desenvolvimento
- SUPER_ADMIN: `superadmin` / `Admin@123`
- TENANT_ADMIN: `admindemo` / `Admin@123`

## Subir ambiente local
```bash
docker compose up -d
./gradlew bootRun
```

## Estrutura de pacotes
- `common`: enums comuns (`Role`, `Status`).
- `config`: segurança e OpenAPI.
- `security`: JWT, filtro de autenticação e `TenantContext`.
- `auth`: login/refresh/me.
- `tenant`: CRUD administrativo de tenants.
- `user`: CRUD de usuários e regras por perfil.
- `audit`: auditoria com JPA Auditing.
- `exception`: tratamento global padronizado.

## Multi-tenancy
- `tenant_id` em `users`.
- `TenantContext` é preenchido pelo JWT autenticado.
- serviços aplicam validação de escopo por tenant para não confiar em dados do frontend.

## Swagger
- URL: `http://localhost:8080/swagger-ui.html`

## Testes
```bash
./gradlew test
```

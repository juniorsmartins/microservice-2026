--liquibase formatted sql
--changeset junior.martins:1
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE IF NOT EXISTS roles (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(50) NOT NULL UNIQUE,
    CONSTRAINT chk_name_not_empty CHECK (name <> '')
);
COMMENT ON TABLE roles IS 'Tabela para mapear permissões/papéis de usuários.'
COMMENT ON COLUMN roles.id IS 'Identificador único gerado automaticamente como UUID.'
COMMENT ON COLUMN roles.name IS 'Nome da permissão (enum como string), não nulo e único.'

CREATE INDEX idx_roles_name ON roles (name);

--rollback DROP EXTENSION pgcrypto;
--rollback DROP INDEX idx_roles_name;
--rollback DROP TABLE roles;

--liquibase formatted sql
--changeset author:junior.martins-1

CREATE TABLE IF NOT EXISTS roles (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(50) NOT NULL UNIQUE,
    created_by VARCHAR(50),
    created_date TIMESTAMP WITH TIME ZONE NOT NULL,
    last_modified_by VARCHAR(50),
    last_modified_date TIMESTAMP WITH TIME ZONE,
    CONSTRAINT chk_name_not_empty CHECK (name <> '')
);

COMMENT ON TABLE roles IS 'Tabela para mapear permissões/papéis de usuários.';
COMMENT ON COLUMN roles.id IS 'Identificador único gerado automaticamente como UUID.';
COMMENT ON COLUMN roles.name IS 'Nome da permissão (enum como string), não nulo e único.';
COMMENT ON COLUMN roles.created_date IS 'Data e hora de criação do registro do cliente para auditoria.';
COMMENT ON COLUMN roles.last_modified_date IS 'Data e hora da última modificação do registro do cliente para auditoria.';
COMMENT ON COLUMN roles.created_by IS 'Define quem criou o registro do cliente para auditoria.';
COMMENT ON COLUMN roles.last_modified_by IS 'Define quem modificou o registro do cliente pela última vez para auditoria.';

CREATE INDEX idx_roles_name ON roles (name);

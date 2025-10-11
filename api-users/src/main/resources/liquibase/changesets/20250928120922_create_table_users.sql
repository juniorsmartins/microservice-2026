--liquibase formatted sql
--changeset author:junior.martins-2

CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(200) NOT NULL,
    role_id UUID NOT NULL,
    CONSTRAINT fk_users_roles FOREIGN KEY (role_id) REFERENCES roles (id)
);

COMMENT ON TABLE users IS 'Tabela para mapear usuários.';
COMMENT ON COLUMN users.id IS 'Identificador único gerado automaticamente como UUID.';
COMMENT ON COLUMN users.username IS 'Username do Usuário para login no sistema. Não pode ser nulo e deve ser único.';
COMMENT ON COLUMN users.password IS 'Password do Usuário para acesso ao sistema. Não pode ser nulo.';
COMMENT ON COLUMN users.role_id IS 'Chave estrangeira da tabela roles. Aponta qual a permissão do usuário.';

CREATE INDEX idx_users_username ON users (username);

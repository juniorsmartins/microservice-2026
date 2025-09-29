--liquibase formatted sql
--changeset author:junior.martins-3

CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE IF NOT EXISTS customers (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    user_id UUID NOT NULL,
    CONSTRAINT fk_customers_users FOREIGN KEY (user_id) REFERENCES users (id)
);

COMMENT ON TABLE customers IS 'Tabela para o cliente da aplicação.';
COMMENT ON COLUMN customers.id IS 'Identificador único gerado automaticamente como UUID.';
COMMENT ON COLUMN customers.name IS 'Nome do cliente, não nulo.';
COMMENT ON COLUMN customers.email IS 'Email do cliente para comunicação, não nulo e único.';
COMMENT ON COLUMN customers.user_id IS 'Chave estrangeira da tabela users. Aponta qual o usuário do cliente.';

CREATE INDEX idx_customers_email ON customers (email);

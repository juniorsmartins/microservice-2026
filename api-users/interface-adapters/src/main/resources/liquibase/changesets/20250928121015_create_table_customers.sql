--liquibase formatted sql
--changeset author:junior.martins-3

CREATE TABLE IF NOT EXISTS customers (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(50) NOT NULL,
    email VARCHAR NOT NULL UNIQUE,
    user_id UUID NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_customers_users FOREIGN KEY (user_id) REFERENCES users (id)
);

COMMENT ON TABLE customers IS 'Tabela para o cliente da aplicação.';
COMMENT ON COLUMN customers.id IS 'Identificador único gerado automaticamente como UUID.';
COMMENT ON COLUMN customers.name IS 'Nome do cliente, não nulo.';
COMMENT ON COLUMN customers.email IS 'Email do cliente para comunicação, não nulo e único.';
COMMENT ON COLUMN customers.user_id IS 'Chave estrangeira da tabela users. Aponta qual o usuário do cliente.';
COMMENT ON COLUMN customers.active IS 'Indica se o cliente está ativo. Padrão: TRUE.';

CREATE INDEX idx_customers_email ON customers (email);

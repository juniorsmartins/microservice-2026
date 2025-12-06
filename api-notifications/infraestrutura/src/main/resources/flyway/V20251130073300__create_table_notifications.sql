CREATE TABLE IF NOT EXISTS notifications (
    id CHAR(36) PRIMARY KEY NOT NULL DEFAULT (UUID()) COMMENT 'Chave primária auto-incremental.',
    customer_code CHAR(36) NOT NULL COMMENT 'UUID do cliente no sistema.',
    customer_email VARCHAR(255) NOT NULL COMMENT 'E-mail do cliente no momento do envio.',
    message TEXT NOT NULL COMMENT 'Texto da mensagem enviada ao cliente.',
    reason VARCHAR(50) NOT NULL COMMENT 'Motivo para o envio do email.',
    created_at TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT 'Data e hora em UTC do envio da notificação.'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX IF NOT EXISTS index_notifications_customer_code ON notifications (customer_code);
CREATE INDEX IF NOT EXISTS index_notifications_customer_email ON notifications (customer_email);

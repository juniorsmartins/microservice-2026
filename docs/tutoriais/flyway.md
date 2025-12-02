# TUTORIAL

## 1. Teoria

### Fontes:
- https://docs.spring.io/spring-boot/how-to/data-initialization.html#howto.data-initialization.migration-tool.flyway 
- https://documentation.red-gate.com/fd/quickstart-gradle-184127577.html
- https://documentation.red-gate.com/fd/flyway-namespace-277578913.html 
- https://docs.spring.io/spring-boot/api/rest/actuator/flyway.html 
- https://www.baeldung.com/database-migrations-with-flyway
- https://www.baeldung.com/liquibase-vs-flyway 
- https://www.baeldung.com/flyway-roll-back 
- https://www.baeldung.com/flyway-callbacks 
- https://www.baeldung.com/spring-boot-flyway-repair 
- https://medium.com/javarevisited/flyway-migrations-naming-strategy-in-a-big-project-288155a01532
- https://mariadb.org/documentation/
- https://mariadb.com/docs/server/mariadb-quickstart-guides/mariadb-sql-cheat-cheat-guide
- https://mariadb.com/docs/server/reference/data-types/date-and-time-data-types/datetime 
- https://mariadb.com/docs/server/reference/sql-statements/data-definition/create/create-index 
- https://mariadb.com/docs/server/reference/data-types/string-data-types/character-sets/supported-character-sets-and-collations 


### Introdução: 

```
O Flyway estende o DevOps para seus bancos de dados para acelerar a entrega de software e 
garantir código de qualidade. O Flyway ajuda você a versar as alterações do banco de dados 
e automatizar as implantações do banco de dados com segurança e facilidade. 

O cenário mais fácil é quando você usa o Flyway em um banco de dados vazio. Flyway 
tentará localizar a tabela de histórico de esquemas no banco de dados, como está vazio, o 
Flyway não o encontrará e o criará. Agora você tem um banco de dados com uma única tabela 
vazia chamada flyway_schema_history por padrão. Esta tabela é usada para rastrear as 
alterações no banco de dados.

O Flyway irá  verificar o sistema de arquivos ou o caminho de classe do aplicativo para 
migrações. Eles podem ser escritos em SQL, Java ou outras linguagens de script. As migrações 
são aplicadas em ordem com base no seu número de versão. À medida que cada migração é 
aplicada, a tabela de histórico do esquema é atualizada. 

COMANDOS: o Flyway suporta os seguintes comandos básicos para gerenciar as migrações de 
banco de dados:

    * Info: imprime o status/versão atual de um esquema de banco de dados. Ele imprime 
    quais migrações estão pendentes, quais migrações foram aplicadas, o status das 
    migrações aplicadas e quando foram aplicadas;
    
    * Migrar: migra um esquema de banco de dados para a versão atual. Ele verifica o caminho 
    de classe para migrações disponíveis e aplica migrações pendentes;
    
    * Linha de base: Linhas de base de um banco de dados existente, excluindo todas as 
    migrações, incluindo baselineVersion. A linha de base ajuda a começar com o Flyway em 
    um banco de dados existente. Migrações mais recentes podem então ser aplicadas 
    normalmente;
    
    * Validar: Valida o esquema de banco de dados atual contra migrações disponíveis;

    * Reparação: Repara a tabela de metadados;
    
    * Limpo: solta todos os objetos em um esquema configurado. É claro que nunca devemos 
    usar limpo em qualquer banco de dados de produção.
```

## 2. Configuração 

### Passo-a-passo

1. Adicionar no build.gradle;
   2. Plugin;
   3. Dependências.
2. Criar diretório em resource (flyway);
3. Configurar application.yml (default e test);
4. Criar Scripts (SQL e etc);

### Implementação: 

build.gradle 
```

```

application.yml (default - flyway ativado)
```

```

application.yml (test - flyway desativado)
```

```

Script (exemplo)
```
CREATE TABLE IF NOT EXISTS notifications (
    id CHAR(36) PRIMARY KEY NOT NULL DEFAULT (UUID()) COMMENT 'Chave primária auto-incremental',
    customer_code CHAR(36) NOT NULL COMMENT 'UUID do cliente no sistema',
    customer_email VARCHAR(255) NOT NULL COMMENT 'E-mail do cliente no momento do envio',
    message TEXT NOT NULL COMMENT 'Texto da mensagem enviada ao cliente',
    reason VARCHAR(50) NOT NULL COMMENT 'Motivo para o envio do email.',
    created_at DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT 'Data e hora exata do envio da notificação'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX IF NOT EXISTS index_notifications_customer_code ON notifications (customer_code);
CREATE INDEX IF NOT EXISTS index_notifications_customer_email ON notifications (customer_email);
```


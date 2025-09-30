PASSO-A-PASSO (SPRING)

1. build.grade

```
implementation 'org.liquibase:liquibase-core'
```

2. Diretórios

Criar os diretórios liquibase.changelog e liquibase.changesets em resources;
```
resources
    liquibase
        changelog
        changesets
```

3. Configuração de application.yml (default e test) 

Ambiente default
```   
spring:  
    liquibase:  
        enabled: true  
        change-log: classpath:liquibase/changelog/master.yaml  
        default-schema: public

    jpa: 
        hibernate:
            ddl-auto: none
        defer-datasource-initialization: false
```
Ambiente de test
```   
spring:  
    liquibase:  
        enabled: false 

    jpa: 
        hibernate:
            ddl-auto: create-drop
        defer-datasource-initialization: true
```

4. Arquivo de changelog

Criar o arquivo liquibase/changelog/master.yaml

Exemplo (a indentação é importante):
```
databaseChangeLog:
    - include:  
        file: liquibase/changesets/20250928120810_create_table_roles.sql
    - include:  
        file: liquibase/changesets/20250928120922_create_table_users.sql
    - include:  
        file: liquibase/changesets/20250928121015_create_table_customers.sql
```
O formato recomendado para o nome do arquivo (data e hora + ação): 
YYYYMMDDHHMMSS_descricao_legivel_da_mudanca.sql

4. Script/changesets

Exemplo em SQL
```
--liquibase formatted sql  
--changeset author:junior.martins-1  

CREATE EXTENSION IF NOT EXISTS "pgcrypto";    

CREATE TABLE IF NOT EXISTS roles (  
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),  
    name VARCHAR(50) NOT NULL UNIQUE,  
    CONSTRAINT chk_name_not_empty CHECK (name <> '')  
);
  
COMMENT ON TABLE roles IS 'Tabela para mapear permissões/papéis de usuários.';  
COMMENT ON COLUMN roles.id IS 'Identificador único gerado automaticamente como UUID.';
COMMENT ON COLUMN roles.name IS 'Nome da permissão (enum como string), não nulo e único.';  
CREATE INDEX idx_roles_name ON roles (name);
```



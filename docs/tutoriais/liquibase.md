TUTORIAL

Fontes:
- https://docs.liquibase.com/oss/user-guide-4-33/intro-to-liquibase 
- https://docs.liquibase.com/pro/user-guide-4-33/what-is-a-changelog 
- https://docs.liquibase.com/reference-guide/changelog-attributes/what-are-contexts 
- https://docs.liquibase.com/pro/user-guide-4-33/what-is-a-change-type 
- https://docs.liquibase.com/reference-guide/changelog-attributes/what-are-labels 
- https://docs.liquibase.com/pro/user-guide-4-33/what-are-preconditions 
- https://docs.liquibase.com/oss/user-guide-4-33/what-is-the-liquibase-properties-file


Teoria: 
```
O Liquibase é uma solução de gerenciamento de alterações de esquema de banco de dados que 
permite revisar e liberar mudanças de banco de dados mais rapidamente e mais seguro desde 
o desenvolvimento até a produção.

Para começar a usar o Liquibase de forma rápida e fácil, você pode escrever seus scripts 
de migração em SQL.

Para tirar proveito das habilidades de abstração de banco de dados que permitem escrever 
alterações uma vez e implantar em diferentes plataformas de banco de dados, você pode 
especificar alterações agnósticas de banco de dados em XML, JSON ou YAML.

- Changelog: o Liquibase usa arquivos de changelog (SQL, XML, JSON e YAML) para listar as 
alterações de banco de dados sequencialmente. 

- Changesets: os Changesets contêm Tipos de alteração, que são tipos de operações a serem 
aplicadas ao banco de dados, como adicionar uma coluna ou chave primária. 

```

Changelog
```
Você usa um arquivo de changelog baseado em texto para listar sequencialmente todas as 
alterações feitas em seu banco de dados. Esse livro-razão ajuda o Liquibase a auditar 
seu banco de dados e executar quaisquer alterações que ainda não foram aplicadas. Você 
pode armazenar e versar seu changelog em qualquer ferramenta de controle de origem.

Uma unidade individual de mudança no seu changelog é chamada de Changeset. Ao modificar 
seu banco de dados, basta adicionar um novo conjunto de alterações e especificar sua 
operação como um Tipo de Alteração. Por exemplo, você pode adicionar um conjunto de 
alterações para criar uma nova tabela e outro conjunto de alterações para soltar uma 
chave primária.

Você também pode incluir outros changelogs em um changelog principal, minimizando conflitos 
entre diferentes equipes ou fluxos de trabalho. Você também pode especificar pré-condições, 
contextos, rótulos e outros atributos no changelog para controlar com precisão quais 
conjuntos de alterações são executados e em quais ambientes.

O Liquibase usa a tabela DATABASECHANGELOG para acompanhar quais alterações implantou. O 
Liquibase também registra informações adicionais de migração na tabela 
DATABASECHANGELOGHISTORY.
```

PASSO-A-PASSO 

1. build.grade

```
implementation 'org.springframework.boot:spring-boot-starter-liquibase'
testImplementation 'org.springframework.boot:spring-boot-starter-liquibase-test'
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

5. Script/changesets

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



# TUTORIAL

## 1. Teoria

### Fontes:
- https://documentation.red-gate.com/fd/quickstart-gradle-184127577.html
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


### Introdução: 

```
O Flyway estende o DevOps para seus bancos de dados para acelerar a entrega de software e 
garantir código de qualidade. O Flyway ajuda você a versar as alterações do banco de dados 
e automatizar as implantações do banco de dados com segurança e facilidade. 

O cenário mais fácil é quando você usa  o Flyway  em um  banco de dados vazio. Flyway 
tentará localizar a tabela de histórico de esquemas no banco de dados. Como está vazio, o 
Flyway não o encontrará e o criará. Agora você tem um banco de dados com uma única tabela 
vazia chamada flyway_schema_history por padrão. Esta tabela é usada para rastrear as 
alterações no banco de dados.

O Flyway irá  verificar o sistema de arquivos ou o caminho de classe do aplicativo para 
migrações. Eles podem ser escritos em SQL, Java ou outras linguagens de script. As migrações 
são aplicadas em ordem com base no seu número de versão. À medida que cada migração é 
aplicada, a tabela de histórico do esquema é atualizada.





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






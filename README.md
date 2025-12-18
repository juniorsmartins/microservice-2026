# PROJETO: currículo vivo

O projeto utiliza Arquitetura de Microsserviços, com comunicação Assíncrona Não Bloqueante 
e colaboração Orientada a Eventos. 

## Autoria

[Junior Martins](https://www.linkedin.com/in/juniorsmartins/)

## Índice

1. Arquitetura 
2. [API - Users](#api---users)
3.  [API - Accounts](#api---accounts)
4.  [API - Investments](#api---investments)
5.  [API - Notifications](#api---notifications)
6.  [API - Reports](#api---reports)
7.  [Tutoriais](#tutoriais)
8.  [Como rodar a aplicação](#como-rodar-a-aplicacao) 
9.  [Coleções de teste para Postman](#colecoes-de-teste-para-postman)

## Arquitetura 

Arquitetura de Microsserviços

Arquitetura Orientada a Eventos

## API - Users

### Tecnologias Users

- Java (versão 25);
- Gradle (versão 9.1.0);
- Spring Boot (versão 4.0.0);
- Spring Data WebMVC;
- Spring Data JPA;
- Spring Data Envers (auditoria - versão 4.0.1);
- Spring Boot Actuator (monitoramento);
- Liquibase (migration em SQL);
- Apache Kafka, Schema Registry e Apache Avro (mensageria);
- RestAssured, JUnit e Mockito (testes);
- PostgreSQL (versão 17 - banco de dados relacional);
- H2 Database (banco de dados em memória para testes);
- Docker (dockerfile e docker compose);
- Lombok.

### Diagramas Users

Arquitetura Limpa multi-modulo 
![Arquitetura Limpa multi-modulo](docs/diagramas/api-users/ARQ-USERS-v2.png)

Diagrama Entidade Relacionamento - DER 
![Diagrama Entidade Relacionamento](docs/diagramas/api-users/DER-api-users.png)

## Config Server

### Tecnologias ConfigServer

- Java (versão 25);
- Gradle (versão 9.1.0);
- Spring Boot Actuator (monitoramento);
- Spring Cloud Config Server;
- Docker (dockerfile e docker compose);

## API - Notifications

### Tecnologias Notifications 

- Java (versão 25);
- Gradle (versão 9.1.0);
- Spring Boot (versão 4.0.0);
- Spring Data WebMVC;
- Spring Data JPA;
- Spring Mail;
- Flyway (migration em SQL);
- Apache Kafka, Schema Registry e Apache Avro (mensageria);
- RestAssured, JUnit e Mockito (testes);
- MariaDB (banco de dados relacional);
- H2 Database (banco de dados em memória para testes);
- Docker (dockerfile e docker compose);
- Lombok.

## Tutoriais

- [Gradle multi-modulo](docs/tutoriais/multimodulo.md);
- [Kafka](docs/tutoriais/kafka.md);
- [Liquibase](docs/tutoriais/liquibase.md);
- [Flyway](docs/tutoriais/flyway.md);
- [Spring Mail](docs/tutoriais/spring-mail.md); 
- [Spring Cloud](docs/tutoriais/spring-cloud.md);
- [Auditoria](docs/tutoriais/auditoria.md);
- [Padrões e Utilidades](docs/tutoriais/padroes_utils.md)


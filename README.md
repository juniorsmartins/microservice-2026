# PROJETO: currículo vivo

O projeto utiliza Arquitetura de Microsserviços, com comunicação Assíncrona Não 
Bloqueante e colaboração Orientada a Eventos. 

## Autoria

[Junior Martins](https://www.linkedin.com/in/juniorsmartins/)

## Índice

1. Arquitetura 
2.  [API-Users](docs/README-api-users.md)
3.  [API-Notifications](docs/README-api-notifications.md)
4.  [API-News](docs/README-api-news.md)
5.  [Config Server](#config-server)
6.  [Eureka Server](#eureka-server)
7.  [Gateway](#gateway)
8.  [Tutoriais](#tutoriais)
9.  [FAQ - Principais dúvidas](#faq---principais-dúvidas) 

## Arquitetura 

Arquitetura de Microsserviços
![Arquitetura de Microsserviços](docs/diagramas/Microservice-v3.jpg)

Arquitetura Orientada a Eventos
![Arquitetura Orientada a Eventos](docs/diagramas/ArquiteturaEventos-v1.jpg)


## Config Server

Microsserviço responsável por centralizar as configurações dos microsserviços.

### Tecnologias ConfigServer

- Java (versão 25);
- Gradle (versão 9.2.1);
- Spring Boot (versão 4.0.0);
- Spring Boot Actuator (monitoramento);
- Spring Cloud Config Server (versão 2025.1.0);
- Docker (dockerfile e docker compose);


## Eureka Server

Microsserviço responsável pelo registro e descoberta dos microsserviços.

### Tecnologias EurekaServer

- Java (versão 25);
- Gradle (versão 9.2.1);
- Spring Boot (versão 4.0.0);
- Spring Boot Actuator (monitoramento);
- Spring Cloud Netflix Eureka Server (versão 2025.1.0);
- Docker (dockerfile e docker compose);


## Gateway

Microsserviço responsável por rotear as requisições para os microsserviços.

### Tecnologias Gateway

- Java (versão 25);
- Gradle (versão 9.2.1);
- Spring Boot (versão 4.0.0);
- Docker (dockerfile e docker compose);


## Tutoriais

- [Gradle multi-modulo](docs/tutoriais/multimodulo.md);
- [Kafka](docs/tutoriais/kafka.md);
- [Liquibase](docs/tutoriais/liquibase.md);
- [Flyway](docs/tutoriais/flyway.md);
- [Spring Mail](docs/tutoriais/spring-mail.md); 
- [Config Server](docs/tutoriais/config-server.md);
- [Eureka Server](docs/tutoriais/eureka-server.md);
- [Gateway](docs/tutoriais/gateway.md);
- [Resiliência](docs/tutoriais/resiliencia.md);
- [Auditoria](docs/tutoriais/auditoria.md);
- [Padrões e Utilidades](docs/tutoriais/padroes-utils.md);
- [Spring Data AOT](docs/tutoriais/NOT-spring-data-aot.md)
- [API Versioning](docs/tutoriais/api-versioning.md);


## FAQ - Principais dúvidas

1. Como rodar a aplicação?
```
1. Abra o terminal do seu notebook com o comando: Ctrl + Alt + t;
2. No terminal, rode o comando: git clone git@github.com:juniorsmartins/microservice-2026.git 
3. Abra sua IDE e abre o projeto clonado;
4. Abra o terminal da IDE;
5. Rode o comando: cd docker
6. Rode o comando: docker compose up --build 
7. Pronto! Sua aplicação subirá em alguns segundos.
```

2. Como acessar a documentação viva por Spring Doc?
```
1. Depois de rodar a aplicação (pergunta 1 do FAQ), acesse a URL: 
```


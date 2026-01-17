# PROJETO: currículo vivo

- Objetivo: microsserviços com ferramentas de pesquisa, estatística, investigação e publicação de material jornalístico. 


## Autoria

[Junior Martins](https://www.linkedin.com/in/juniorsmartins/)

## Índice

1. Arquitetura 
2.  [API-Users - clique aqui](docs/README-api-users.md)
3.  [API-Notifications - clique aqui](docs/README-api-notifications.md)
4.  [API-News - clique aqui](docs/README-api-news.md)
5.  [API-IAs - clique aqui](docs/README-api-ias.md)
6.  [Config Server](#config-server)
7.  [Eureka Server](#eureka-server)
8.  [Gateway](#gateway)
9.  [Tutoriais](#tutoriais)
10.  [FAQ - Principais dúvidas](#faq---principais-dúvidas) 

## Arquitetura 

Arquitetura de Microsserviços
![Arquitetura de Microsserviços](docs/diagramas/Microservice-v3.jpg)

Arquitetura Orientada a Eventos
![Arquitetura Orientada a Eventos](docs/diagramas/ArquiteturaEventos-v1.jpg)


## Config Server

Descrição: Microsserviço responsável por centralizar as configurações dos microsserviços.

### Tecnologias ConfigServer

- Java (versão 25);
- Gradle (versão 9.2.1);
- Spring Boot (versão 4.0.1);
- Spring Boot Actuator (monitoramento);
- Spring Cloud Config Server (versão 2025.1.0);
- Spring Cloud Bus AMQP (versão 2025.1.0);
- Docker (dockerfile e docker compose).


## Eureka Server

Descrição: Microsserviço responsável pelo registro e descoberta dos microsserviços.

### Tecnologias EurekaServer

- Java (versão 25);
- Gradle (versão 9.2.1);
- Spring Boot (versão 4.0.1);
- Spring Boot Actuator (monitoramento);
- Spring Cloud Netflix Eureka Server (versão 2025.1.0);
- Docker (dockerfile e docker compose).


## Gateway

Descrição: Microsserviço responsável por rotear as requisições para os microsserviços.

### Tecnologias Gateway

- Java (versão 25);
- Gradle (versão 9.2.1);
- Spring Boot (versão 4.0.1);
- Spring Boot Actuator (monitoramento);
- Spring Cloud Gateway Server WebFlux (versão 2025.1.0);
- Spring Cloud Netflix Eureka Client (versão 2025.1.0);
- Spring Doc OpenApi (versão 3.0.0);
- Docker (dockerfile e docker compose).


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
- [Spring Doc](docs/tutoriais/spring-doc.md);
- [API Versioning](docs/tutoriais/api-versioning.md);


## FAQ - Principais dúvidas

1. Meu computador precisa ter quais dependências para rodar a aplicação?
```
1. Docker;
2, Docker Compose.
```

2. Como rodar a aplicação?
```
1. Abra o terminal do seu notebook com o comando: Ctrl + Alt + t;
2. No terminal, rode o comando: git clone git@github.com:juniorsmartins/microservice-2026.git 
3. Abra sua IDE e abre o projeto clonado;
4. Abra o terminal da IDE;
5. Rode o comando: cd docker
6. Rode o comando: docker compose up --build 
7. Pronto! Sua aplicação subirá em alguns segundos.
```

3. Como acessar a documentação viva por Spring Doc?
```
1. Depois de rodar a aplicação, verifique se os serviços estão ativos no EurekaServer: http://localhost:8761/ 
2. Há duas estratégias para acessar a dccumentação: 
    a. Acesse a url de documentação de cada API: 
        a1. http://localhost:9050/swagger-ui/v3/index.html 
        a2. http://localhost:9000/swagger-ui/v3/index.html 
        a3. http://localhost:9060/swagger-ui/v3/index.html
        a4. http://localhost:9010/swagger-ui/v3/index.html
    b. Acesse a url da documentação via GatewayServer: http://localhost:8765/swagger-ui/index.html
        - Pelo GatewayServer, a documentação de todas as APIs pdde ser escolhida em "Select a definition" (canto superior direito da tela).
```

4. Como testar o microsserviços manualmente? 

Estratégia 1
```
1. Acesse a documentação de cada API e faça testes por meio dela:
    a. http://localhost:9050/swagger-ui/v3/index.html 
    b. http://localhost:9000/swagger-ui/v3/index.html 
    c. http://localhost:9060/swagger-ui/v3/index.html
    d. http://localhost:9010/swagger-ui/v3/index.html
```

Estratégia 2 (link da coleção do postman - ainda não disponibilizei !!!??? )
```
1. Pode baixar um arquivo de coleções do Postman. Ele possui requisições prontas para testar;
2. Importar esse arquivo de coleções no seu Postman;
3. Testar.
```




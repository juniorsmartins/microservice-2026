# TUTORIAL

## 1. Teoria

### Fontes:
- https://spring.io/projects/spring-cloud-bus 
- https://docs.spring.io/spring-cloud-bus/reference/spring-cloud-bus/bus-endpoints.html 
- https://docs.spring.io/spring-cloud-bus/reference/appendix.html 
- https://docs.spring.io/spring-cloud-config/docs/current/reference/html/#_quick_start 
- https://www.baeldung.com/spring-cloud-bus 
- https://www.baeldung.com/spring-cloud-config-without-git 
- https://www.youtube.com/watch?v=ais9i16sAjg

### Introdução: 

```
O Spring Cloud Bus conecta os nós de um sistema distribuído a um broker de mensagens leve. Esse broker 
pode então ser usado para transmitir mudanças de estado (como alterações de configuração) ou outras 
instruções de gerenciamento. A ideia principal é que o bus funcione como um atuador distribuído para 
uma aplicação Spring Boot escalável horizontalmente. No entanto, ele também pode ser usado como um 
canal de comunicação entre aplicações. Este projeto fornece modelos iniciais para um broker AMQP ou 
Kafka como meio de transporte.

O Spring Cloud Bus fornece três endpoints via Actuator: 

- O /actuator/busrefreshendpoint limpa o RefreshScopecache e reassocia os dados @ConfigurationProperties;
- O /actuator/busenvendpoint atualiza o ambiente de cada instância com o par chave/valor especificado em várias instâncias;
- O /actuator/busshutdown encerra o plicativo.
```

## 2. Configuração

### Passo-a-passo

ConfigServer:
1. Adicionar dependências no build.gradle:
   - a. Spring Cloud Bus com Kafka ou com RabbitMQ (já temos o Spring Cloud Actuator).
2. Configurar application.yml:
  - a. Configuração do Bus;
  - b. Configuração do Actuator (busenv, busrefresh e busshutdown);
  - c. Configuração da mensageria.
3. Fazer Post no ConfigServer para propagar mudanças:
  - a. Exemplo: http://localhost:8888/actuator/busrefresh

Cliente:
1. Adicionar dependências no build.gradle:
   - a. Spring Cloud Bus com Kafka ou com RabbitMQ (já temos o Spring Cloud Config Client).
2. Configurar application.yml:
  - a. Configuração do Bus;
  - b. Configuração do Actuator (busenv, busrefresh e busshutdown);
  - c. Configuração da mensageria.

IMPORTANTE: Não defina profiles dentro dos arquivos de configuração disponíveis no repositório remoto do ConfigServer. 
Exemplo:
```
spring:
  profiles:
    active: dev
```
Como esses arquivos já possuem o profile no nome do arquivo (ex: api-users-dev.yml), eles não podem ter isso dentro do arquivo.

### Implementação: 

ConfigServer:
1. Adicionar dependências no build.gradle:
```
implementation 'org.springframework.cloud:spring-cloud-starter-bus-amqp'
```
2. Configurar application.yml:
```
spring:
  application:
    name: configserver

  profiles:
    active: git 

  config:
    name: ${spring.application.name}
  cloud:
    config:
      enabled: false 
      server:
        git: 
          uri: https://github.com/juniorsmartins/microservice-2026-config-server.git
          skipSslValidation: true 
          default-label: master 
          deleteUntrackedBranches: true 
          timeout: 5 
          clone-on-start: true 
          force-pull: true 
          ignore-application: true
        bootstrap: false 

    bus:
      enabled: true
      trace:
        enabled: true
      env:
        enabled: true

  rabbitmq:
    host: ${RABBIT_HOST:localhost}
    port: ${RABBIT_PORT:5672}
    username: ${RABBIT_USER:"guest"}
    password: ${RABBIT_PASS:"guest"}

management:
  endpoints:
    web:
      base-path: /actuator 
      exposure:
        include: refresh,health,info,metrics,configprops,busenv,busrefresh,busshutdown 
```

Cliente:
1. Adicionar dependências no build.gradle:
```
implementation 'org.springframework.cloud:spring-cloud-starter-bus-amqp'
```
2. Configurar application.yml:
```
spring:
  application:
    name: api-ias

  profiles:
    active: default

  config:
    name: ${spring.application.name} 
    import: optional:configserver:${SPRING_CLOUD_CONFIG_SERVER_URI:http://localhost:8888} 
  cloud:
    config:
      name: ${spring.application.name} 
      profile: ${SPRING_PROFILES_ACTIVE:prod} 
      fail-fast: true 
      timeout: 120
      refresh:
        enable: true 
      retry: 
        initial-interval: 2500 
        max-interval: 6000 
        max-attempts: 2 
    bus: 
      enabled: true 
      refresh: 
        enabled: true 
      env: 
        enabled: true 

  rabbitmq:
    host: ${RABBIT_HOST:localhost}
    port: ${RABBIT_PORT:5672}
    username: ${RABBIT_USER:"guest"}
    password: ${RABBIT_PASS:"guest"}

management:
  endpoints:
    web:
      base-path: /actuator 
      exposure:
        include: refresh,health,info,metrics,configprops,busenv,busrefresh,busshutdown 
```




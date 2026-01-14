# TUTORIAL

## 1. Teoria

### Fontes:
- https://spring.io/projects/spring-cloud-bus 
- https://docs.spring.io/spring-cloud-bus/reference/spring-cloud-bus/bus-endpoints.html 
- https://docs.spring.io/spring-cloud-bus/reference/appendix.html 
- https://docs.spring.io/spring-cloud-config/docs/current/reference/html/#_quick_start 
- 
- https://www.baeldung.com/spring-cloud-bus 
- https://www.baeldung.com/spring-cloud-config-without-git 
- 
- 
- https://www.youtube.com/watch?v=ais9i16sAjg 
- 

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
   - a. Spring Cloud Bus com Kafka ou com RabbitMQ;
   - a. Spring Cloud Config Monitor;
   - b. Spring Boot Actuator.
2. Configurar application.yml:
  - a. Configuração do Bus;
  - b. Configuração do Actuator (busenv, busrefresh e busshutdown).
  - c. Configuração da mensageria.
3. Configuração de Webhook no GitHub (repositório de configurações):
  - a. Acesse o repositório de configurações no GitHub;
  - b. Vá em "Settings" > "Webhooks" > "Add webhook";
  - c. No campo "Payload URL", insira a URL do endpoint /monitor do Config Server (exemplo: http://localhost:8888/monitor);
  - d. No campo "Content type", selecione "application/json";
  - e. Em "Which events would you like to trigger this webhook?", selecione "Just the push event.";
  - f. Clique em "Add webhook" para salvar.

Cliente:
1. Adicionar dependências no build.gradle:
   - a. Spring Cloud Bus com Kafka ou com RabbitMQ;
   - b. Spring Boot Actuator.
2. Configurar application.yml:
  - a. Configuração do Bus;
  - b. Configuração do Actuator (busenv, busrefresh e busshutdown).
  - c. Configuração da mensageria.


### Implementação: 


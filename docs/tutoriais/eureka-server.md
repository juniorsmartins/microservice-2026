# TUTORIAL

## 1. Teoria

### Fontes:
- https://docs.spring.io/spring-cloud-netflix/reference/spring-cloud-netflix.html
- 

### Introdução: 
```

```

## 2. Configuração

### Passo-a-passo

Cliente
1. Adiciona dependência no build.gradle:
   - a. Spring Boot Actuator;
   - b. Spring Cloud Netflix Eureka Client;
2. Configurar application.yml;
   - Ambientes de trabalho;
   - Ambiente de testes (desativa).

Servidor
1. Criar o projeto Spring com duas dependências:
   - a. Spring Boot Actuator;
   - b. Spring Cloud Netflix Eureka Server;
2. Habilitar o Eureka Server na classe principal Main com a anotação @EnableEurekaServer;
3. Configurar application.yml padrão do Eureka Server:
   - a. Configurar porta do servidor Eureka;
   - b. Configurar nome do servidor Eureka;
   - c. Configurar actuator;
   - d. Configurar Eureka;


### Implementação: 

```

```



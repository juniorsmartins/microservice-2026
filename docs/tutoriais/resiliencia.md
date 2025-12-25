# TUTORIAL

## 1. Teoria

### Fontes:
- https://www.youtube.com/watch?v=CT1wGTwOfg0 
- https://www.baeldung.com/spring-retry 
- 
- https://spring.io/projects/spring-cloud-circuitbreaker 
- https://docs.spring.io/spring-cloud-circuitbreaker/reference/ 
- https://github.com/spring-projects/spring-retry 
- https://github.com/resilience4j/resilience4j 
- https://resilience4j.readme.io/docs/getting-started-3 
- 
- https://www.baeldung.com/resilience4j 
- 
- 
- 

### Introdução: 
```
O Spring Cloud Circuit Breaker oferece uma abstração entre diferentes 
implementações de disjuntores. Ele fornece uma API consistente para uso em nas 
aplicações, permitindo escolher a implementação de disjuntor que melhor atenda às 
necessidades do aplicativo.

Implementações suportadas: 
- Resilience4j;
    a. Resilience4j (spring-cloud-starter-circuitbreaker-resilience4j);
    b. Resilience4j Reactive (spring-cloud-starter-circuitbreaker-reactor-resilience4j).
- Spring Retry (spring-cloud-starter-circuitbreaker-spring-retry).

Configuração automática: pode desativar a autoconfiguração do Resilience4J 
definindo spring.cloud.circuitbreaker.resilience4j.enabled como false.
```

## 2. Configuração

### Passo-a-passo

API
1. Adicionar anotação @EnableResilientMethods na classe Main;
2. Adicionar anotação @Retryable nos métodos da classe Controller;
3. Três formas: criar classe para configuração programática da Retentativa (mais 
flexível); ou configurar a própria anotação @Retryable do controller; ou configurar 
via application.yml;
4. 

### Implementação: 

```
@Retryable(
    maxRetries = 3, 
    delay = 1000, 
    multiplier = 2 
)
```

```

```

```

```
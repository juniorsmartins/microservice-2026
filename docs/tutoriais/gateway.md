# TUTORIAL

## 1. Teoria

### Fontes:
- https://www.baeldung.com/spring-cloud-series 
- https://spring.io/projects/spring-cloud-gateway#overview 
- https://docs.spring.io/spring-cloud-gateway/reference/spring-cloud-gateway-server-webmvc/starter.html 
- https://docs.spring.io/spring-cloud-gateway/reference/spring-cloud-gateway-server-webflux/starter.html 
- https://docs.spring.io/spring-cloud-gateway/reference/spring-cloud-gateway-server-webflux/how-it-works.html 
- https://www.baeldung.com/spring-cloud-gateway 
- https://www.baeldung.com/spring-cloud-gateway-routing-predicate-factories 
- https://www.baeldung.com/spring-cloud-gateway-webfilter-factories 
- 

- https://www.baeldung.com/spring-cloud-gateaway-configure-cors-policy 
- https://www.baeldung.com/spring-cloud-gateway-integrate-openapi 
- https://www.baeldung.com/spring-cloud-global-exception-handling 


- https://www.youtube.com/watch?v=-XZgwcs6YEU 
- https://www.youtube.com/watch?v=ju7NTqJxKRs 
- https://www.youtube.com/watch?v=aJXVuNW8Fcc 


### Introdução: 
```
O Spring Cloud Gateway visa oferecer uma maneira simples, porém eficaz, de rotear 
requisições para APIs e fornecer a elas recursos transversais como segurança, 
monitoramento/métricas e resiliência.
```

## 2. Configuração

### Passo-a-passo

Servidor
1. Criar um projeto com dependências: 
   - a. Spring Cloud Gateway (WebFlux ou WebMVC);
   - b. Spring Boot Actuator (opcional);
   - c. Spring Cloud Netflix Eureka Client.
2. Configurar o application.yml:
   - a. Configurar porta do servidor e nome da aplicação; 
   - b. Configurar propriedade de Gateway;
   - c. Configurar propriedade do Actuator (opcional);
   - d. Configurar propriedade do Eureka Client;
3. Testar via Postman ou Curl: 
   - a. Endereço <gateway><nome-aplicação><endpoint-aplicação> (localhost:8765/API-USERS/v1/customers);
   - b. Configurar application para usar minúscula no nome da aplicação (localhost:8765/api-users/v1/customers);
4. Pode ou não criar classe com Bean de configuração personalizada de rotas;


### Implementação: 

```

```


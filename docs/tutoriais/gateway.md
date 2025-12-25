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
   - a. Endereço <gateway><endpoint-aplicação> (localhost:8765/v1/customers);
   - b. Configurar application para usar minúscula no nome da aplicação (localhost:8765/v1/customers);
4. Opcional - Pode ou não criar classe com Bean de configuração programática de rotas;
5. Opcional - Pode ou não criar um filtro global (GlobalFilter) para registrar logs de requisições.


### Implementação: 

Servidor
1. Criar um projeto com dependências:
   - a. Spring Cloud Gateway (WebFlux ou WebMVC);
   - b. Spring Boot Actuator (opcional);
   - c. Spring Cloud Netflix Eureka Client.
```
plugins {
	id 'java'
	id 'org.springframework.boot' version '4.0.0'
	id 'io.spring.dependency-management' version '1.1.7'
}

group = 'backend.communication'
version = '0.0.1-SNAPSHOT'
description = 'Demo project for Spring Boot'

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(25)
	}
}

repositories {
	mavenCentral()
}

ext {
	set('springCloudVersion', "2025.1.0")
}

dependencies {
	implementation 'org.springframework.cloud:spring-cloud-starter-gateway-server-webflux'
	implementation 'org.springframework.boot:spring-boot-starter-actuator'
	testImplementation 'org.springframework.boot:spring-boot-starter-actuator-test'
	implementation 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-client'

	testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
}

dependencyManagement {
	imports {
		mavenBom "org.springframework.cloud:spring-cloud-dependencies:${springCloudVersion}"
	}
}

tasks.named('test') {
	useJUnitPlatform()
}
```
2. Configurar o application.yml:
   - a. Configurar porta do servidor e nome da aplicação;
   - b. Configurar propriedade de Gateway;
   - c. Configurar propriedade do Actuator (opcional);
   - d. Configurar propriedade do Eureka Client;
```
server:
  port: ${SERVER_PORT:8765}

spring:
  application:
    name: gatewayserver

  cloud:
    gateway:
      server:
        webflux:
          discovery:
            locator:
              enabled: false # Se for true, cria rotas automaticamente. Elimina a necessidade de bean para configurar rotas.
              lower-case-service-id: true 

management:
  endpoints:
    web:
      base-path: /actuator 
      exposure:
        include: refresh,health,info,metrics,configprops 
  endpoint:
    health:
      show-details: always 
    configprops:
      show-values: always 

eureka:
  client:
    register-with-eureka: true 
    fetch-registry: true 
    service-url:
      defaultZone: ${SPRING_CLOUD_EUREKA_SERVER_URI:http://localhost:8761/eureka/} 
    healthcheck: 
      enabled: true
```
4. Opcional - Pode ou não criar classe com Bean de configuração programática de rotas;
```
@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {

        return builder.routes() 

                .route("api-users", p -> p 
                        .path("**/v1/customers**") 
                        .uri("lb://api-users")) 

                .route("api-notifications", p -> p
                        .path("**/v1/notifications**")
                        .uri("lb://api-notifications"))

                .build();
    }
}
```
5. Opcional - Pode ou não criar um filtro global (GlobalFilter) para registrar logs de requisições.
```
@Component
public class LoggingFilter implements GlobalFilter {

    private final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        logger.info("Requisição recebida - Método: {} Path: {} Uri: {}",
                exchange.getRequest().getMethod(),
                exchange.getRequest().getPath(),
                exchange.getRequest().getURI());

        return chain.filter(exchange);
    }
}
```


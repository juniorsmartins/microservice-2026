# TUTORIAL

## 1. Teoria

### Fontes:
- https://spring.io/projects/spring-cloud#learn 
- https://docs.spring.io/spring-cloud-release/reference/index.html 
- https://docs.spring.io/spring-cloud-netflix/reference/spring-cloud-netflix.html
- https://www.baeldung.com/spring-cloud-netflix-eureka 
- https://docs.spring.io/spring-cloud-netflix/docs/current/reference/html/ 
- https://spring.io/guides/gs/service-registration-and-discovery 
- https://www.youtube.com/watch?v=ju7NTqJxKRs 
- https://www.youtube.com/watch?v=054sZsBc8ao&t=522s 
- https://javafullstackdev.medium.com/how-to-use-spring-boot-eureka-server-in-spring-boot-3-3-0-794e8d173d9e 

### Introdução: 
```
O Spring Cloud Netflix Eureka é um serviço de descoberta que permite que os microsserviços 
se registrem e descubram uns aos outros em um ambiente distribuído. Ele é amplamente 
utilizado em arquiteturas de microsserviços para facilitar a comunicação entre serviços. 

Quando um microsserviço inicia, ele se registra no servidor Eureka, fornecendo informações 
sobre sua localização (endereço IP, porta, etc.). Outros microsserviços podem então 
consultar o servidor Eureka para descobrir a localização dos serviços registrados, 
permitindo que eles se comuniquem de forma dinâmica. O Eureka também oferece recursos de 
balanceamento de carga e tolerância a falhas, ajudando a garantir que os serviços 
permaneçam disponíveis mesmo em caso de falhas.
```

## 2. Configuração

### Passo-a-passo

Cliente
1. Adiciona dependência no build.gradle:
   - a. Spring Boot Actuator (opcional);
   - b. Spring Cloud Netflix Eureka Client;
2. Configurar application.yml;
   - Ambientes de trabalho;
   - Ambiente de testes (desativa).

Servidor
1. Criar o projeto Spring com duas dependências:
   - a. Spring Boot Actuator (opcional);
   - b. Spring Cloud Netflix Eureka Server;
2. Habilitar o Eureka Server na classe principal Main com a anotação @EnableEurekaServer;
3. Configurar application.yml padrão do Eureka Server:
   - a. Configurar porta do servidor Eureka;
   - b. Configurar nome do servidor Eureka;
   - c. Configurar actuator;
   - d. Configurar Eureka;
   - e. Configurar logging.


### Implementação: 

Cliente
1. Adiciona dependência no build.gradle:
   - a. Spring Boot Actuator (opcional);
   - b. Spring Cloud Netflix Eureka Client;
```
ext {
    set('springCloudVersion', "2025.1.0")
}

dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-actuator'
    testImplementation 'org.springframework.boot:spring-boot-starter-actuator-test'
    implementation 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-client'
}

dependencyManagement {
    imports {
        mavenBom "org.springframework.cloud:spring-cloud-dependencies:${springCloudVersion}"
    }
}
```

2. Configurar application.yml;
   - Ambientes de trabalho;
   - Ambiente de testes (desativa).
```
management:
  endpoints:
    web:
      base-path: /actuator 
      exposure:
        include: refresh,health,info,metrics,configprops,flyway 
  endpoint:
    health:
      show-details: always 
    configprops:
      show-values: always 

eureka:
  client:
    register-with-eureka: true 
    fetch-registry: false 
    service-url:
      defaultZone: ${SPRING_CLOUD_EUREKA_SERVER_URI:http://localhost:8761/eureka/} 
    healthcheck:
      enabled: true
```
Teste
```
management:
  endpoints:
    web:
      base-path: /actuator 
      exposure:
        include: refresh,health,info,metrics,configprops,flyway 
  endpoint:
    health:
      show-details: always 
    configprops:
      show-values: always 

eureka:
  client:
    enabled: false
```

Servidor
1. Criar o projeto Spring com duas dependências:
   - a. Spring Boot Actuator (opcional);
   - b. Spring Cloud Netflix Eureka Server;
```
ext {
	set('springCloudVersion', "2025.1.0")
}

dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-actuator'
	implementation 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-server'
	testImplementation 'org.springframework.boot:spring-boot-starter-actuator-test'
	testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
}

dependencyManagement {
	imports {
		mavenBom "org.springframework.cloud:spring-cloud-dependencies:${springCloudVersion}"
	}
}
```
2. Habilitar o Eureka Server na classe principal Main com a anotação @EnableEurekaServer;
```
@SpringBootApplication
@EnableEurekaServer
public class EurekaserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekaserverApplication.class, args);
	}

}
```
3. Configurar application.yml padrão do Eureka Server:
   - a. Configurar porta do servidor Eureka;
   - b. Configurar nome do servidor Eureka;
   - c. Configurar actuator;
   - d. Configurar Eureka;
   - e. Configurar logging.
```
spring:
  application:
    name: eurekaserver

eureka:
  client:
    register-with-eureka: false 
    fetch-registry: false 

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

logging:
  level:
    com.netflix.eureka: OFF
    com.netflix.discovery: OFF
```


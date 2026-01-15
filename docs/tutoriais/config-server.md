# TUTORIAL

## 1. Teoria

### Fontes:
- https://www.udemy.com/course/microservices-do-0-a-gcp-com-spring-boot-kubernetes-e-docker/ 
- https://spring.io/projects/spring-cloud-config 
- https://docs.spring.io/spring-cloud-config/docs/current/reference/html/#_quick_start 
- https://docs.spring.io/spring-boot/api/rest/actuator/index.html#page-title 
- https://docs.spring.io/spring-boot/api/rest/actuator/liquibase.html 
- https://docs.spring.io/spring-cloud-config/reference/server/environment-repository/git-backend.html 
- https://spring.io/guides/gs/centralized-configuration 
- https://www.youtube.com/watch?v=upoIwn4rWCo&list=PLqq-6Pq4lTTaoaVoQVfRJPqvNTCjcTvJB 
- https://www.youtube.com/watch?v=JSdy9Q8Uk34&list=PLqq-6Pq4lTTaoaVoQVfRJPqvNTCjcTvJB&index=10
- https://www.youtube.com/watch?v=gb1i4WyWNK4&list=PLqq-6Pq4lTTaoaVoQVfRJPqvNTCjcTvJB&index=11 
- https://www.youtube.com/watch?v=E2HkL766VHs 
- https://www.youtube.com/watch?v=yNnLICy2zk4 
- https://www.youtube.com/watch?v=AiGCx0raQfs 
- https://www.youtube.com/watch?v=20KlqncKmw8 
- https://docs.spring.io/spring-cloud-config/reference/server.html 

### Introdução: 

Spring Boot Actuator
```
É um conjunto de ferramentas "prontas para produção" que permite que você monitore e 
gerencie sua aplicação sem precisar escrever código extra para isso.

Imagine que sua aplicação é um carro: o código de negócio é o motor, mas o Actuator é o 
painel de instrumentos (velocímetro, temperatura, nível de óleo) e o scâner do mecânico.
```
Spring Cloud Config
```
Spring Cloud Config é uma solução do ecossistema Spring que resolve um dos principais 
desafios no desenvolvimento de aplicações baseadas em microsserviços: a centralização e 
gestão eficiente de configurações. Em ambientes de microsserviços, cada serviço pode ter 
suas próprias configurações, o que pode levar a inconsistências e dificuldades na 
manutenção. O Spring Cloud Config permite que as aplicações obtenham suas configurações de 
um repositório centralizado, facilitando a gestão e atualização dessas configurações sem a
necessidade de redeploy das aplicações. 

Composto por dois componentes principais: o servidor de configuração (Config Server) e o 
cliente de configuração (Config Client). O Config Server atua como um ponto central onde as 
configurações são armazenadas, geralmente em repositórios Git, SVN ou sistemas de arquivos. 
Já o Config Client é integrado às aplicações Spring Boot, permitindo que elas busquem suas 
configurações do Config Server durante a inicialização ou em tempo de execução.
```

## 2. Configuração

### Passo-a-passo

Servidor
1. Criar projeto Spring com apenas duas dependências (ficará no monorepo das demais aplicações):
   a. Spring Boot Actuator; 
   b. Spring Cloud Config Server;
2. Habilitar o Config Server na classe principal Main com a anotação @EnableConfigServer;
3. Criar novo repositório Git exclusivo para armazenar os arquivos de configuração;
   a. Criar os arquivos de configuração seguindo a nomenclatura. Optei por colocar na raíz do repositório (sem diretórios);
   b. Nome dos arquivos: {application-name}-{profile}.yml (exemplo: api-users-dev.yml ou api-users-prod.yml);
4. Configurar application.yml padrão do Config Server:
   a. Apontar repositório Git para buscar arquivos de configuração;
   b. Configurar endpoints do Actuator no application.yml padrão;

Cliente
1. Adicionar dependência no build.gradle:
  - a. Spring Boot Actuator;
  - b. Spring Cloud Config Client;
2. Configurar application.yml: 
   - Ambientes de trabalho (precisa de refresh no actuator);
   - Ambiente de testes.
3. Adicionar anotação @RefreshScope (+ @Primary) em todos os Beans que deseja atualizar em tempo de execução;
4. Fazer POST no endpoint /actuator/refresh para atualizar as configurações em tempo de execução;

IMPORTANTE: use obrigatoriamente o "optional" no endereço do ConfigServer. O ConfigServer apresentou problema quando
não usei o optional nos arquivos de configuração do repositório remoto.
Aqui nesse trecho: spring.config.import: optional:configserver:${SPRING_CLOUD_CONFIG_SERVER_URI:http://localhost:8888} 

Testar
1. http://localhost:9000/api/v1/customers/prod

### Implementação: 

Servidor
1. build.gradle:
```
plugins {
	id 'java'
	id 'org.springframework.boot' version '4.0.1'
	id 'io.spring.dependency-management' version '1.1.7'
}

group = 'backend.communication'
version = '0.0.1-SNAPSHOT'
description = 'Microsserviço responsável por centralizar as configurações dos microsserviços.'

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
	implementation 'org.springframework.cloud:spring-cloud-config-server'
	implementation 'org.springframework.boot:spring-boot-starter-actuator'
	testImplementation 'org.springframework.boot:spring-boot-starter-actuator-test'

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
2. Habilitar o Config Server na classe principal Main com a anotação @EnableConfigServer;
```
@SpringBootApplication
@EnableConfigServer
public class ConfigserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigserverApplication.class, args);
	}

}
```
4. Configurar application.yml padrão do Config Server:
```
spring:
  application:
    name: configserver
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

management:
  endpoints:
    web:
      base-path: /actuator 
      exposure:
        include: refresh,health,info,metrics
```

Cliente
1. Adicionar dependência no build.gradle:
```
 implementation 'org.springframework.boot:spring-boot-starter-actuator'
 testImplementation 'org.springframework.boot:spring-boot-starter-actuator-test'
 implementation 'org.springframework.cloud:spring-cloud-starter-config'
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
        multiplier: 1.1

management:
  endpoints:
    web:
      base-path: /actuator
      exposure:
        include: refresh,health,info,metrics
```
Ambiente de teste
```
spring:
  config:
    import: optional:configserver:${SPRING_CLOUD_CONFIG_SERVER_URI:http://localhost:8888}
  cloud:
    config:
      enabled: false
    discovery:
      enabled: false
```
3. Adicionar anotação @RefreshScope (+ @Primary) em todos os Beans que deseja atualizar em tempo de execução;
```
@Configuration
public class DataSourceConfig {

    @Bean
    @Primary 
    @RefreshScope 
    @ConfigurationProperties("spring.datasource.hikari")
    public HikariDataSource dataSource(DataSourceProperties properties) {

        HikariDataSource dataSource = properties.initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();

        if (properties.getUrl() != null) {
            dataSource.setJdbcUrl(properties.getUrl());
        }

        return dataSource;
    }
}
```
4. Fazer POST no endpoint /actuator/refresh para atualizar as configurações em tempo de execução;
```
localhost:9050/actuator/refresh
```



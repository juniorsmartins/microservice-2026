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

Cliente
1. Adicionar dependência no build.gradle:
  - a. Spring Boot Actuator;
  - b. Spring Cloud Config Client;
2. Configurar application.yml: 
   - Ambientes de trabalho;
   - Ambiente de testes.
3. Adicionar anotação @RefreshScope (+ @Primary) em todos os Beans que deseja atualizar em tempo de execução;
4. Adicionar "refresh" na configuração de endpoints do Actuator no application.yml;
5. Fazer POST no endpoint /actuator/refresh para atualizar as configurações em tempo de execução;

Servidor
1. Criar projeto Spring com apenas duas dependências (ficará no monorepo das demais aplicações):
  - a. Spring Boot Actuator;
  - b. Spring Cloud Config Server;
2. Habilitar o Config Server na classe principal Main com a anotação @EnableConfigServer;
3. Criar novo repositório Git exclusivo para armazenar os arquivos de configuração;
   a. Criar diretório, com o nome da api, onde serão colocados os respectivos arquivos de configuração;
   b. Criar os arquivos de configuração que serão consumidos pela aplicação cliente;
   c. Nome dos arquivos: {application-name}-{profile}.yml (configuração específica para cada aplicação e perfil,
   como dev, prod e etc.);
4. Configurar application.yml padrão do Config Server:
  - a. Apontar repositório Git para buscar arquivos de configuração;
  - b. Configurar endpoints do Actuator no application.yml padrão;


### Implementação: 

Cliente
1. build.gradle:
  a. Spring Boot Actuator
```
implementation 'org.springframework.boot:spring-boot-starter-actuator' 
testImplementation 'org.springframework.boot:spring-boot-starter-actuator-test'
```
  b. Spring Cloud Config Client
```
ext {
    set('springCloudVersion', "2025.1.0")
}

dependencies {
    implementation 'org.springframework.cloud:spring-cloud-starter-config' 
}

dependencyManagement {
    imports {
        mavenBom "org.springframework.cloud:spring-cloud-dependencies:${springCloudVersion}"
    }
}
```
2. Configurar application.yml:
```
spring:
  application:
    name: api-users
  config:
    import: optional:configserver:${SPRING_CLOUD_CONFIG_SERVER_URI:http://localhost:8888} 
  cloud:
    config:
      profile: ${SPRING_PROFILES_ACTIVE:prod} 
      fail-fast: true 
      refresh:
        enable: true 
      retry: 
        initial-interval: 3000 
        max-interval: 9000 
        max-attempts: 3 

management:
  endpoints:
    web:
      base-path: /actuator 
      exposure:
        include: refresh,health,info,metrics,liquibase,configprops 
  endpoint:
    health:
      show-details: always 
    configprops:
      show-values: always
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
4. Adicionar "refresh" na configuração de endpoints do Actuator no application.yml;
5. Fazer POST no endpoint /actuator/refresh para atualizar as configurações em tempo de execução;
```
localhost:9050/actuator/refresh
```

Servidor
1. Criar projeto Spring com apenas duas dependências
- a. Spring Boot Actuator;
- b. Spring Cloud Config Server;
```
ext {
	set('springCloudVersion', "2025.1.0")
}

dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-actuator'
	implementation 'org.springframework.cloud:spring-cloud-config-server'
	testImplementation 'org.springframework.boot:spring-boot-starter-actuator-test'
	testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
}

dependencyManagement {
	imports {
		mavenBom "org.springframework.cloud:spring-cloud-dependencies:${springCloudVersion}"
	}
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
3. Criar novo repositório Git exclusivo para armazenar os arquivos de configuração;
   a.Criar diretório, com o nome da api, onde serão colocados os respectivos arquivos de configuração;
   b. Criar os arquivos de configuração que serão consumidos pela aplicação cliente;
   c. Nome dos arquivos: {application-name}-{profile}.yml (configuração específica para cada aplicação e perfil,
   como dev, prod e etc.);
```
exemplo: https://github.com/juniorsmartins/microservice-2026-config-server
- nome do diretório: api-users
- nome do arquivo: api-users-dev.yml
- nome do arquivo: api-users-prod.yml
```
4. Configurar application.yml padrão do Config Server:
- a. Apontar repositório Git para buscar arquivos de configuração;
- b. Configurar endpoints do Actuator no application.yml padrão;
```
spring:
  application:
    name: configserver
  profiles:
    active: git 

  cloud:
    config:
      server:
        git: 
          uri: https://github.com/juniorsmartins/microservice-2026-config-server
          default-label: master 
          deleteUntrackedBranches: true 
          timeout: 5 
          clone-on-start: true 
          force-pull: true 
          search-paths: 
            - 'api-users*'
            - 'api-notifications*'
```



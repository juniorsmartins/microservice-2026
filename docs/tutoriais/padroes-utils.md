# TUTORIAL

## Data e Hora (ISO-8601)

* Fontes:
- https://www.iso.org/iso-8601-date-and-time-format.html 
- https://docs.spring.io/spring-boot/reference/features/external-config.html
- https://www.baeldung.com/java-format-localdate-iso-8601-t-z
- https://www.baeldung.com/java-jackson-offsetdatetime 
- https://www.baeldung.com/spring-boot-set-default-timezone 
- https://www.baeldung.com/java-jvm-time-zone
- https://en.wikipedia.org/wiki/ISO_8601 
- https://docs.spring.io/spring-boot/reference/web/servlet.html#web.servlet.spring-mvc.conversion-service 
- https://www.youtube.com/watch?v=aAUopejsqIc 

A ISO-8601 é uma norma internacional para representar datas e horas em um formato claro, 
inequívoco e universalmente aceito. Esta norma ISO ajuda a remover dúvidas que podem 
resultar das várias convenções, culturas e fusos horários que afetam uma operação global. 
Ela dá uma maneira de apresentar datas e horários que é claramente definido e compreensível 
para pessoas e máquinas.

A ISO 8601 aborda essa incerteza estabelecendo uma maneira internacionalmente acordada de 
representar as datas: YYYY-MM-DD

Portanto, a ordem dos elementos utilizados para expressar data e hora na ISO 8601 é a 
seguinte: ano, mês, dia, hora, minutos, segundos e milissegundos. Por exemplo, 
27 de setembro de 2022 às 18h está representado como 2022-09-27 18:00:00.000.

Padrão é: aceita qualquer fuso horário como entrada, armazenar em UTC e retornar em UTC.
```
Formato: AAAA-MM-DDThh:mm:ss.sssZ 

Segue abaixo a descrição dos componentes:

    AAAA : Representa o ano com quatro dígitos (ex.: 2023)
    MM : Representa o mês com dois dígitos (ex.: 03 para março)
    DD : Representa o dia do mês com dois dígitos (ex.: 15)
    'T': Um caractere ' T ' literal que separa a data da hora.
    hh : Representa a hora do dia no formato de 24 horas (ex.: 14 para 14h).
    mm : Representa os minutos (ex.: 30)
    ss : Representa os segundos (ex.: 45)
    sss : Representa milissegundos (opcional e pode variar em duração)
    'Z' : Um caractere 'Z' literal que indica que a hora está em Tempo Universal 
    Coordenado (UTC).
```

* Passo-a-passo: 

1. Add serverTimeZone (serverTimezone=UTC) na url do datasource (UTC no database);
2. Add TimeZone (TimeZone.setDefault(TimeZone.getTimeZone("UTC"))) na Main (UTC na aplicação).

* Configuração:

1. application.yml
```
url: jdbc:mariadb://${DB_HOST:localhost}:${DB_PORT:3306}/${DB_NAME:db-notifications}?serverTimezone=UTC
```
2. Classe Main
```
static void main(String[] args) {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    SpringApplication.run(ApiNotificationsApplication.class, args);
}
```

## Pool de Conexões

* Fontes:
- https://www.baeldung.com/java-connection-pooling 
- https://www.baeldung.com/hikaricp 
- https://github.com/brettwooldridge/HikariCP 
- https://www.baeldung.com/spring-boot-hikari 
- https://docs.spring.io/spring-boot/reference/data/sql.html#data.sql.datasource.connection-pool 
- https://docs.spring.io/spring-boot/how-to/data-access.html 
- https://docs.oracle.com/en/java/javase/17/docs/api/java.sql/javax/sql/DataSource.html 
- https://dev.mysql.com/doc/connector-j/en/connector-j-reference-configuration-properties.html 
- https://github.com/brettwooldridge/HikariCP/wiki/Hibernate4 
- https://github.com/brettwooldridge/HikariCP/wiki/MySQL-Configuration 
- https://medium.com/@AlexanderObregon/how-spring-boot-optimizes-database-connections-through-hikaricp-f19be1a246d7 

O pool de conexões é um padrão de acesso a dados bem conhecido. Seu principal objetivo é 
reduzir a sobrecarga envolvida na realização de conexões de banco de dados e operações de 
banco de dados de leitura / gravação. No nível mais básico, um pool de conexão é uma 
implementação de cache de conexão de banco de dados que pode ser configurada para atender a 
requisitos específicos.

Se analisarmos a sequência de etapas envolvidas em um ciclo de vida de conexão de banco de 
dados típico, entenderemos o porquê:

    1. Abrindo uma conexão com o banco de dados usando o driver de banco de dados
    2. Abrindo um soquete TCP para dados de leitura/escrita
    3. Leitura / gravação de dados sobre a tomada
    4. Fechando a conexão
    5. Fechando o soquete

Torna-se evidente que as conexões de banco de dados são operações bastante caras e, como 
tal, devem ser reduzidas ao mínimo em todos os casos de uso possíveis.

Apenas implementando um contêiner de conexão de banco de dados, que nos permite reutilizar 
uma série de conexões existentes, podemos efetivamente economizar o custo de realizar um 
grande número de viagens de banco de dados caras. Isso aumenta o desempenho geral de nossos 
aplicativos orientados por banco de dados.

O pool de conexões não pode se tornar um gargalo do microsserviço, pois se tornaria um 
problema de escalabilidade e desempenho durante o armazenamento de dados. 

* Passo-a-passo: 

1. Adicionar propriedades no application.yml;

* Configuração:

application.yml
```
spring:
  datasource: 
    driver-class-name: org.postgresql.Driver 
    url: jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5432}/${DB_NAME:db-users}?serverTimezone=UTC 
    username: ${DB_USERNAME:postgres}
    password: ${DB_PASSWORD:postgres}
    hikari:
      autoCommit: false
      pool-name: ${spring.application.name}-hikariPool
      minimum-idle: 10
      maximum-pool-size: 25
      idle-timeout: 600000
      max-lifetime: 1800000
      connection-timeout: 3000
      validation-timeout: 5000
      connection-test-query: SELECT 1
      keepalive-time: 300000
      isolate-internal-queries: true
      data-source-properties:
        cachePrepStmts: true 
        prepStmtCacheSize: 250 
        prepStmtCacheSqlLimit: 2048 
        useServerPrepStmts: true 
        useLocalSessionState: true 
        rewriteBatchedStatements: true 
        cacheResultSetMetadata: true 
        cacheServerConfiguration: true 
        elideSetAutoCommits: true 
        maintainTimeStats: false
```

## API Versioning

* Fontes:
- (Dan Vega) - https://www.youtube.com/watch?v=qjo2tYf01xo&list=PLZV0a2jwt22v874ngZcWw3umP2yfsV9sK&index=12 
- https://www.danvega.dev/blog/spring-boot-4-api-versioning 
- https://github.com/danvega/api-users/blob/master/src/main/java/dev/danvega/users/user/UserController.java 
- https://docs.spring.io/spring-framework/reference/web/webmvc-versioning.html 
- https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-config/api-version.html 
- https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-requestmapping.html#mvc-ann-requestmapping-version 
- https://docs.spring.io/spring-framework/reference/web/webmvc-functional.html#api-version 
- https://docs.spring.io/spring-framework/reference/web/webflux-versioning.html#webflux-versioning-strategy

* Introdução:

O Spring MVC oferece suporte ao versionamento de APIs.  

Quatro estratégias de versionamento:
1. Por cabeçalho (request header);
2. Por parâmetro de consulta (query parameter);
3. Por parâmetro de tipo de mídia (content negotiation - accept header);
4. Por caminho da URL (path-segment - abordagem mais Restful).

Duas estratégias de configuração:
1. Por WebMvcConfigurer;
2. Por propriedades no application.yml ou application.properties.

* Passo-a-passo: 

1. Criar os endpoints com versionamento;
2. Configurar application.yml (estratégia escolhida);
3. Configurar no Gateway.

* Configuração:

1. Criar os endpoints com versionamento;
```
@RestController
@RequestMapping(path = {"/api/"})
@RequiredArgsConstructor
public class NewsController {

    @GetMapping(value = "/{version}/news", version = "1.0")
    public List<NewsResponseV1> findAll() {

        return List.of(new NewsResponseV1("Esporte", "Cowboys vencem Lions",
                "Um touchdown e um extra point para mais", "xpto"));
    }

    @GetMapping(value = "/{version}/news", version = "2.0")
    public List<NewsResponseV2> findAllV2() {

        return List.of(new NewsResponseV2("Esporte", "Cowboys vencem Lions",
                "Um touchdown e um extra point para mais", "xpto", "Gov"));
    }
}
```

2. Configurar application.yml (estratégia escolhida);
```
spring:
  application:
    name: api-news
  mvc:
    apiversion:
      supported: 1.0,2.0
      default: 1.0
      use:
        path-segment: 1
```

3. Configurar no Gateway.
```
@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {

        return builder.routes() 
                .route("api-news", p -> p
                        .path("/api/{version}/news/**")
                        .uri("lb://api-news"))
                .build();
    }
}
```

## Bean Registration - Registro dinâmico de beans com Spring Framework 7 

* Fontes:
- https://docs.spring.io/spring-framework/reference/core/beans/java/programmatic-bean-registration.html#page-title 
- https://www.danvega.dev/blog/programmatic-bean-registration 
- https://github.com/danvega/sb4/tree/master/features/bean-registration 
- https://www.youtube.com/watch?v=yh760wTFL_4 

* Introdução:

A partir do Spring Framework 7, é fornecido suporte de primeira classe para o 
registro programático de beans por meio da BeanRegistrar, uma interface que pode 
ser implementada para registrar beans programaticamente de forma flexível e 
eficiente.

* Passo-a-passo:

1. Criar classe para registrar os beans com BeanRegistrar;
2. Importar o registro na classe de configuração.

* Configuração:

1. Criar classe para registrar os beans com BeanRegistrar;
```
public class UseCaseRegistrar implements BeanRegistrar {

    @Override
    public void register(BeanRegistry registry, Environment env) {

        registry.registerBean("newsCreateUseCase", NewsCreateUseCase.class,
                spec -> spec.description("Serviço de criar News."));
    }
}
```

2. Importar o registro na classe de configuração.
```
@Configuration
@Import(UseCaseRegistrar.class)
public class ModernWebConfig {
}
```

## Null-safety - JSpecify contra nulos com Spring Boot 4

* Fontes:
- https://jspecify.dev/docs/spec/ 
- https://www.danvega.dev/blog/spring-boot-4-null-safety 
- https://www.youtube.com/watch?v=QlGnaRoujL8 
- https://docs.spring.io/spring-framework/reference/core/null-safety.html#null-safety-applications

* Introdução:

A partir do Spring Framework 7, o código-fonte do Spring Framework utiliza 
anotações JSpecify para expor APIs seguras contra valores nulos e verificar a 
consistência dessas declarações de nulidade com o NullAway como parte do processo 
de compilação.

O Spring Boot 4 muda o jogo com a adoção do JSpecify, substituindo as antigas 
anotações JSR-305. Isso não é apenas uma troca de biblioteca — é uma mudança 
fundamental em como lidamos com a segurança contra nulos em aplicações Spring.

O Spring Boot 4 introduz um conceito simples, porém poderoso: não nulo por padrão. 
Em vez de assumir que tudo pode ser nulo (e adicionar verificações defensivas de 
nulo em todos os lugares), você marca explicitamente as exceções — as coisas que 
podem ser nulas.

* Passo-a-passo:

1. Criar arquivo package-info.java (para cada pacote onde quer usar;
2. Adicionar a anotação @NullMarked (em todas as classes onde quer usar - não aceitará nulo por padrão);
3. Opcional - Pode usar a anotação @Nullable onde quiser aceitar nulo.

* Configuração:

1. Criar arquivo package-info.java (para cada pacote onde quer usar;
```
@NullMarked
package backend.core.api_news.controllers;

import org.jspecify.annotations.NullMarked;
```

2. Adicionar a anotação @NullMarked (em todas as classes onde quer usar);
```
@Slf4j
@NullMarked
@RestController
@RequestMapping(path = {"/api/"})
@RequiredArgsConstructor
public class NewsController {

    private final NewsCreateInputPort newsCreateInputPort;

    private final NewsMapperPort newsMapperPort;

    @PostMapping(value = "/{version}/news", version = "1.0")
    public ResponseEntity<NewsCreateResponseV1> createV1(@RequestBody NewsCreateRequestV1 requestV1) {

        var response = Optional.of(requestV1)
                .map(newsMapperPort::toNewsCreateDto)
                .map(newsCreateInputPort::create)
                .map(newsMapperPort::toNewsCreateResponseV1)
                .orElseThrow();

        return ResponseEntity
                .created(URI.create("/1.0/news/" + response.id()))
                .body(response);
    }
}
```

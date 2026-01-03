# TUTORIAL

## 1. Teoria

### Fontes:
- (Dan Vega) - https://www.youtube.com/watch?v=qjo2tYf01xo&list=PLZV0a2jwt22v874ngZcWw3umP2yfsV9sK&index=12
- https://www.danvega.dev/blog/spring-boot-4-api-versioning
- https://github.com/danvega/api-users/blob/master/src/main/java/dev/danvega/users/user/UserController.java
- https://docs.spring.io/spring-framework/reference/web/webmvc-versioning.html
- https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-config/api-version.html
- https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-requestmapping.html#mvc-ann-requestmapping-version
- https://docs.spring.io/spring-framework/reference/web/webmvc-functional.html#api-version
- https://docs.spring.io/spring-framework/reference/web/webflux-versioning.html#webflux-versioning-strategy
- https://www.baeldung.com/spring-boot-4-spring-framework-7
- https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/servlet/config/annotation/ApiVersionConfigurer.html

### Introdução: 
```
O Spring MVC oferece suporte ao versionamento de APIs.  

Quatro estratégias de versionamento:
1. Por cabeçalho (request header);
2. Por parâmetro de consulta (query parameter);
3. Por parâmetro de tipo de mídia (content negotiation - accept header);
4. Por caminho da URL (path-segment - abordagem mais Restful).

Duas estratégias de configuração:
1. Por WebMvcConfigurer;
2. Por propriedades no application.yml ou application.properties.
```
## 2. Configuração

### Passo-a-passo

A. Estratégia "Por caminho da URL" com "Por propriedades no application.yml"
1. Criar os endpoints com versionamento;
2. Configurar application.yml (estratégia 1 escolhida);
3. Configurar rotas no application do GatewayServer;
4. Criar uma classe ApiVersionParser para flexibilizar o uso de versão com "v" ou "V";

B. Estratégia "Por caminho da URL" com "Por WebMvcConfigurer"
1. Criar os endpoints com versionamento;
2. Criar classe ApiVersionConfig, que implementa WebMvcConfigurer (estratégia 2 escolhida); 
3. Configurar rotas no application do GatewayServer;
4. Criar uma classe ApiVersionParser para flexibilizar o uso de versão com "v" ou "V";

IMPORTANTE: incluir versão "3.0" na configuração para que o versionamento não bloqueie o endpoints de Json 
do Spring Doc (/api-news/v3/api-docs). Foi a estratégia mais simples para não gerar conflito entre a 
API Versioning e o Spring Doc. Sem incluir a versão "3.0", com classe ApiVersionParser, não seria possível 
ter ambos em funcionamento.

### Implementação: 

A. Estratégia "Por caminho da URL" com "Por propriedades no application.yml"

A.1. Criar os endpoints com versionamento;
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

A.2. Configurar application.yml (estratégia escolhida);
```
spring:
  application:
    name: api-news
  mvc:
    apiversion:
      supported: 1.0,2.0,3.0
      default: 1.0
      use:
        path-segment: 1
```

A.3. Configurar rotas no application do GatewayServer;
```
spring:
  application:
    name: gatewayserver

  cloud:
    gateway:
      server:
        webflux:
          routes:
            - id: api-users
              uri: lb://api-users
              predicates:
                - Path=/api/{version}/customers/**
            - id: api-notifications
              uri: lb://api-notifications
              predicates:
                - Path=/api/{version}/notifications/**
            - id: api-news
              uri: lb://api-news
              predicates:
                - Path=/api/{version}/news/**

            # ── Rotas exclusivas para documentação SpringDoc ───────────────────────
            - id: api-users-docs
              uri: lb://api-users
              predicates:
                - Path=/api-users/v3/api-docs

            - id: api-news-docs
              uri: lb://api-news
              predicates:
                - Path=/api-news/v3/api-docs

            - id: api-notifications-docs
              uri: lb://api-notifications
              predicates:
                - Path=/api-notifications/v3/api-docs
```

A.4. Criar uma classe ApiVersionParser para flexibilizar o uso de versão com "v" ou "V";
```
public class ApiVersionParser implements org.springframework.web.accept.ApiVersionParser {

    @Override
    public Comparable parseVersion(String version) {

        // Allow "v1" or "v2" instead of "1.0" or "2.0"
        if (version.startsWith("v") || version.startsWith("V")) {
            version = version.substring(1);
        }

        // Auto-append ".0" for major versions
        if (!version.contains(".")) {
            version = version + ".0";
        }

        return version;
    }
}
```

B. Estratégia "Por caminho da URL" com "Por WebMvcConfigurer"

B.2. Criar classe ApiVersionConfig, que implementa WebMvcConfigurer;
```
@Configuration
public class ApiVersionConfig implements WebMvcConfigurer {

    @Override
    public void configureApiVersioning(ApiVersionConfigurer configurer) {
        configurer.usePathSegment(1) 
                .useMediaTypeParameter(MediaType.APPLICATION_JSON, "version")
                .addSupportedVersions("1.0", "2.0", "3.0") 
                .setDefaultVersion("1.0"); 
    }
}
```


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
2. Configurar application.yml (estratégia escolhida);
3. Configurar no Gateway.

B. Estratégia "Por caminho da URL" com "Por WebMvcConfigurer"
1. Criar os endpoints com versionamento;
2. Criar classe ApiVersionConfig, que implementa WebMvcConfigurer; 
3. Configurar no Gateway;
4. Opcional - Dá para criar uma classe ApiVersionParser para flexibilizar o uso de versão com "v";

### Implementação: 

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
      supported: 1.0,2.0
      default: 1.0
      use:
        path-segment: 1
```

A.3. Configurar no Gateway.
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

B.1. Criar os endpoints com versionamento;
```
@Slf4j
@RestController
@RequestMapping(path = {"/api/"})
@RequiredArgsConstructor
public class NewsController {

    private final NewsCreateInputPort newsCreateInputPort;

    private final NewsQueryPort newsQueryPort;

    private final NewsPresenterPort newsPresenterPort;

    @PostMapping(value = "/{version}/news", version = "1.0")
    public ResponseEntity<NewsCreateResponse> create(@RequestBody NewsCreateRequest request) {

        var response = Optional.of(request)
                .map(newsPresenterPort::toNewsDto)
                .map(newsCreateInputPort::create)
                .map(newsPresenterPort::toNewsCreateResponse)
                .orElseThrow();

        return ResponseEntity
                .created(URI.create("/api/1.0/news/" + response.id()))
                .body(response);
    }

    @GetMapping(value = "/{version}/news", version = "1.0")
    public List<NewsResponse> findByTitleLike(@RequestParam(name = "title") String title) {

        return newsQueryPort.findByTitleLike(title)
                .stream()
                .map(newsPresenterPort::toNewsResponse)
                .toList();
    }
}
```

B.2. Criar classe ApiVersionConfig, que implementa WebMvcConfigurer;
```
@Configuration
public class ApiVersionConfig implements WebMvcConfigurer {

    @Override
    public void configureApiVersioning(ApiVersionConfigurer configurer) {
        configurer.usePathSegment(1) 
                .useMediaTypeParameter(MediaType.APPLICATION_JSON, "version")
                .addSupportedVersions("1.0", "2.0") 
                .setDefaultVersion("1.0"); 
    }
}
```

B.3. Configurar no Gateway.
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

B.4. Opcional - Dá para criar uma classe ApiVersionParser para flexibilizar o uso de versão com "v";
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
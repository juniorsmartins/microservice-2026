# TUTORIAL

## 1. Teoria

### Fontes:
- https://springdoc.org/#Introduction 
- https://github.com/springdoc/springdoc-openapi 
- https://www.baeldung.com/spring-rest-openapi-documentation 
- https://javadoc.io/doc/io.swagger.core.v3/swagger-annotations/latest/index.html 
- (Udemy) - https://www.udemy.com/course/microservices-do-0-a-gcp-com-spring-boot-kubernetes-e-docker/learn/lecture/51023705#overview 
- (Algaworks) - https://app.algaworks.com/aulas/4389/conhecendo-o-springdoc 
- https://stackoverflow.com/questions/75732794/spring-boot-3-and-swagger-ui-java-lang-nosuchmethoderror-io-swagger-v3-oas-ann 
- https://www.baeldung.com/spring-cors 
- https://docs.spring.io/spring-cloud-gateway/reference/spring-cloud-gateway-server-webflux/cors-configuration.html 
- https://www.baeldung.com/spring-webflux-cors 

### Introdução: 

Spring Doc
```
Esta biblioteca Java ajuda a automatizar a geração de documentação de API em 
projetos Spring Boot. Ela funciona examinando a aplicação em tempo de execução 
para inferir a semântica da API com base nas configurações do Spring, na estrutura 
de classes e em diversas anotações. Gera automaticamente documentação de APIs nos 
formatos JSON/YAML e HTML. Essa documentação pode ser complementada com comentários 
usando anotações do swagger-api.
```

## 2. Configuração

### Passo-a-passo

Microsserviço

1. Adicionar dependências no build.gradle;
    a. Spring Doc (duas dependências - a segunda para evitar conflitos com Confluent);
    b. Spring Doc Hateoas (se estiver usando Spring Hateoas);
    c. Spring Doc Security.
2. Criar bean de configuração (openApi);
3. Adicionar anotações de Swagger nos Controllers, DTOs e etc;
4. Criar anotação @PageableParameter para documentar paginação;

Gateway Server

1. Adicionar dependência de SpringDoc no gradle.build do GatewayServer;
2. Adicionar configurações do SpringDoc no application.yml (rotas e etc);
3. Ir nos microsserviços para adicionar propriedade no application.yml para mostrar no Gateway;
   a. Adicionei versão v3 para o Spring Doc não ser barrado pelo API Versioning.

Acessar Swagger para testar funcionamento:
- Via APIs:
  - http://localhost:9050/api-users/v3/api-docs 
  - http://localhost:9050/swagger-ui/v3/index.html 
  - http://localhost:9000/api-news/v3/api-docs 
  - http://localhost:9000/swagger-ui/v3/index.html
  - http://localhost:9060/api-notifications/v3/api-docs
  - http://localhost:9060/swagger-ui/v3/index.html
- Via Gateway:
  - http://localhost:8765/api-users/v3/api-docs 
  - http://localhost:8765/api-news/v3/api-docs 
  - http://localhost:8765/api-notifications/v3/api-docs
  - http://localhost:8765/swagger-ui/index.html


### Implementação: 

Microsserviço

1. Adicionar dependências no build.gradle;
   a. Spring Doc (duas dependências - a segunda para evitar conflitos com Confluent);
   b. Spring Doc Hateoas (se estiver usando Spring Hateoas);
   c. Spring Doc Security.
```
implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:3.0.0'
implementation 'io.swagger.core.v3:swagger-annotations-jakarta:2.2.41'
```

2. Criar bean de configuração (openApi);
```
@Configuration
@Import(RegisterBeanRegistrar.class) 
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
                .components(new Components())
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("Microsserviço API-Users")
                        .version("1.0")
                        .description("Microsserviço de gerenciamento de clientes.")
                        .contact(new io.swagger.v3.oas.models.info.Contact()
                                .name("Junior Martins")
                                .url("https://www.linkedin.com/in/juniorsmartins/")
                        )
                        .license(new io.swagger.v3.oas.models.info.License()
                                .name("Copyright (c) 2025 Junior Martins. All Rights Reserved.")
                        )
                )
                .externalDocs(new io.swagger.v3.oas.models.ExternalDocumentation()
                        .description("Documentação das APIs do Microsserviços.")
                        .url("https://github.com/juniorsmartins/microservice-2026/blob/master/README.md")
                );
    }
}
```

3. Adicionar anotações de Swagger nos Controllers, DTOs e etc;
```
@Tag(name = "Customer", description = "Controlador do recurso Cliente.")
@Slf4j
@NullMarked
@RestController
@RequestMapping(path = {"/api/"})
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerCreateInputPort customerCreateInputPort;

    private final CustomerUpdateInputPort customerUpdateInputPort;

    private final CustomerDisableInputPort customerDisableInputPort;

    private final CustomerQueryInputPort customerQueryInputPort;

    private final CustomerPagePort customerPagePort;

    @Operation(summary = "Cadastrar", description = "Recurso para criar novos clientes.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Created - recurso cadastrado com sucesso.",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CustomerResponse.class))}
                    ),
                    @ApiResponse(responseCode = "400", description = "Bad Request - requisição mal formulada.",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}
                    ),
                    @ApiResponse(responseCode = "409", description = "Conflict - violação de regras de negócio.",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}
                    ),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error - situação inesperada no servidor.",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}
                    )
            }
    )
    @PostMapping(value = "/{version}/customers", version = "1.0")
    public ResponseEntity<CustomerResponse> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Estrutura de transporte para entrada de dados.", required = true)
            @RequestBody CustomerRequest request) {

        var created = customerCreateInputPort.create(request);

        return ResponseEntity
                .created(URI.create("/api/1.0/customers/" + created.id()))
                .body(created);
    }

    @Operation(summary = "Atualizar", description = "Recurso para atualizar clientes.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK - requisição bem sucedida e com retorno.",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CustomerResponse.class))}
                    ),
                    @ApiResponse(responseCode = "400", description = "Bad Request - requisição mal formulada.",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}
                    ),
                    @ApiResponse(responseCode = "404", description = "Not Found - recurso não encontrado.",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}
                    ),
                    @ApiResponse(responseCode = "409", description = "Conflict - violação de regras de negócio.",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}
                    ),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error - situação inesperada no servidor.",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}
                    )
            }
    )
    @PutMapping(value = "/{version}/customers/{id}", version = "1.0")
    @Retryable(
            excludes = {CustomerNotFoundCustomException.class, EmailConflictRulesCustomException.class,
                    UsernameConflictRulesCustomException.class, RoleNotFoundCustomException.class},
            maxRetries = 2,
            jitter = 10,
            delay = 1000,
            multiplier = 2
    )
    public ResponseEntity<CustomerResponse> update(
            @Parameter(name = "id", description = "Identificador único do recurso.", example = "034eb74c-69ee-4bd4-a064-5c4cc5e9e748", required = true)
            @PathVariable(name = "id") final UUID id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Estrutura de transporte para entrada de dados.", required = true)
            @RequestBody CustomerRequest request) {

        var updated = customerUpdateInputPort.update(id, request);

        return ResponseEntity
                .ok()
                .body(updated);
    }

    @Operation(summary = "Desativar", description = "Recurso para desativar clientes.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "No Content - requisição bem sucedida e sem retorno.",
                            content = {@Content(mediaType = "application/json")}
                    ),
                    @ApiResponse(responseCode = "400", description = "Bad Request - requisição mal formulada.",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}
                    ),
                    @ApiResponse(responseCode = "404", description = "Not Found - recurso não encontrado.",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}
                    ),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error - situação inesperada no servidor.",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}
                    )
            }
    )
    @DeleteMapping(value = "/{version}/customers/{id}", version = "1.0")
    public ResponseEntity<Void> disableById(
            @Parameter(name = "id", description = "Identificador único do recurso.", example = "034eb74c-69ee-4bd4-a064-5c4cc5e9e748", required = true)
            @PathVariable(name = "id") final UUID id) {

        customerDisableInputPort.disableById(id);

        return ResponseEntity
                .noContent()
                .build();
    }

    @Operation(summary = "Consultar por ID", description = "Recurso para consultar clientes por ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK - requisição bem sucedida e com retorno.",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CustomerResponse.class))}
                    ),
                    @ApiResponse(responseCode = "400", description = "Bad Request - requisição mal formulada.",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}
                    ),
                    @ApiResponse(responseCode = "404", description = "Not Found - recurso não encontrado.",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}
                    ),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error - situação inesperada no servidor.",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}
                    )
            }
    )
    @GetMapping(value = "/{version}/customers/{id}", version = "1.0")
    @Retryable(
            excludes = {CustomerNotFoundCustomException.class},
            maxRetries = 4, // Número máximo de tentativas de repetição em caso de falha.
            jitter = 10, // Variação aleatória adicionada ao tempo de espera para evitar picos de carga. Fator de "desfocagem" (blur) para evitar a sincronização de rede. Se usar valor 10 (significa geralmente interpretado como +/- 10% de variação)
            delay = 1000, // Primeiro tempo de espera antes de tentar novamente (milliseconds).
            multiplier = 2 // Fator pelo qual o tempo de espera é multiplicado a cada tentativa subsequente. Um multiplicador para o atraso da próxima tentativa de repetição, aplicado ao atraso anterior (a partir de delay()) e também ao jitter() aplicável a cada tentativa.
    )
    public ResponseEntity<CustomerResponse> findById(
            @Parameter(name = "id", description = "Identificador único do recurso.", example = "034eb74c-69ee-4bd4-a064-5c4cc5e9e748", required = true)
            @PathVariable(name = "id") final UUID id) {

        var response = customerQueryInputPort.findActiveById(id);

        return ResponseEntity
                .ok()
                .body(response);
    }

    @Operation(summary = "Paginar todos", description = "Recurso para buscar todos os clientes paginados.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK - requisição bem sucedida e com retorno.",
                            content = {@Content(mediaType = "application/json", array = @ArraySchema(minItems = 0,
                                    schema = @Schema(implementation = CustomerResponse.class)))
                            }
                    ),
                    @ApiResponse(responseCode = "400", description = "Bad Request - requisição mal formulada.",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}
                    ),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error - situação inesperada no servidor.",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}
                    )
            }
    )
    @GetMapping(value = "/{version}/customers", version = "1.0")
    @Retryable(
            maxRetries = 4, // Número máximo de tentativas de repetição em caso de falha.
            jitter = 10, // Variação aleatória adicionada ao tempo de espera para evitar picos de carga. Fator de "desfocagem" (blur) para evitar a sincronização de rede. Se usar valor 10 (significa geralmente interpretado como +/- 10% de variação)
            delay = 1000, // Primeiro tempo de espera antes de tentar novamente (milliseconds).
            multiplier = 2 // Fator pelo qual o tempo de espera é multiplicado a cada tentativa subsequente. Um multiplicador para o atraso da próxima tentativa de repetição, aplicado ao atraso anterior (a partir de delay()) e também ao jitter() aplicável a cada tentativa.
    )
    @PageableParameter
    public ResponseEntity<Page<CustomerAllResponse>> pageAll(
            @Parameter(hidden = true)
            @PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC, page = 0, size = 5) final Pageable paginacao) {

        var responsePage = customerPagePort.pageAll(paginacao);

        return ResponseEntity
                .ok()
                .body(responsePage);
    }

    @GetMapping(value = "/{version}/customers", version = "2.0")
    @Retryable(
            maxRetries = 4, // Número máximo de tentativas de repetição em caso de falha.
            jitter = 10, // Variação aleatória adicionada ao tempo de espera para evitar picos de carga. Fator de "desfocagem" (blur) para evitar a sincronização de rede. Se usar valor 10 (significa geralmente interpretado como +/- 10% de variação)
            delay = 1000, // Primeiro tempo de espera antes de tentar novamente (milliseconds).
            multiplier = 2 // Fator pelo qual o tempo de espera é multiplicado a cada tentativa subsequente. Um multiplicador para o atraso da próxima tentativa de repetição, aplicado ao atraso anterior (a partir de delay()) e também ao jitter() aplicável a cada tentativa.
    )
    @PageableParameter
    public ResponseEntity<Page<CustomerAllResponse>> pageAllV2(
            @Parameter(hidden = true)
            @PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC, page = 0, size = 5) final Pageable paginacao) {

        var responsePage = customerPagePort.pageAll(paginacao);

        return ResponseEntity
                .ok()
                .body(responsePage);
    }
}
```

4. Criar anotação @PageableParameter para documentar paginação;
````
@Target(value = {ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Parameter(
        in = ParameterIn.QUERY, name = "page", description = "Número da página inicial (0..N)",
        schema = @Schema(type = "integer", defaultValue = "0")
)
@Parameter(
        in = ParameterIn.QUERY, name = "size", description = "Quantidade de elementos por página.",
        schema = @Schema(type = "integer", defaultValue = "5")
)
@Parameter(
        in = ParameterIn.QUERY, name = "sort", description = "Critério de ordenação de página. Exemplos: createdDate,desc ou createdDate,asc."
)
public @interface PageableParameter {
}
````

Gateway Server

1. Adicionar dependência de SpringDoc no gradle.build do GatewayServer;
```
implementation 'org.springdoc:springdoc-openapi-starter-webflux-ui:3.0.0'
```

2. Adicionar configurações do SpringDoc no application.yml (rotas e etc);
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

springdoc:
  swagger-ui:
    urls:
      - name: api-users
        url: /api-users/v3/api-docs
      - name: api-notifications
        url: /api-notifications/v3/api-docs
      - name: api-news
        url: /api-news/v3/api-docs
```

3. Ir nos microsserviços para adicionar propriedade no application.yml para mostrar no Gateway.
```
springdoc:
  api-docs:
    enabled: true
    path: /api-users/v3/api-docs
  swagger-ui:
    enebled: true
    path: /swagger-ui/v3/index.html
    url: /api-users/v3/api-docs
```


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

Gateway Server

1. Adicionar dependência de SpringDoc no gradle.build do GatewayServer;
2. Criar classe de configuração (OpenApiConfig);
3. Adicionar configurações do SpringDoc no application.yml;
4. Ir nos microsserviços para adicionar propriedade no application.yml (permitir mostrar via Gateway).

Acessar Swagger para testar funcionamento:
- Via APIs:
  - http://localhost:9050/api-users/v3/api-docs 
  - http://localhost:9050/swagger-ui/index.html 
  - http://localhost:9000/api-news/v3/api-docs 
  - http://localhost:9000/swagger-ui/index.html 
- Via Gateway:
  - http://localhost:8765/api-users/v3/api-docs 
  - http://localhost:8765/api-news/v3/api-docs 
  - http://localhost:8765/swagger-ui/index.html


### Implementação: 

1. Adicionar dependência no build.gradle;
```
implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:3.0.0'
implementation 'io.swagger.core.v3:swagger-annotations-jakarta:2.2.41'
```

2. Criar bean de configuração (openApi);
```
@Configuration
@Import(RegisterBeanRegistrar.class)
public class WebConfig {

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
@Tag(name = "CustomerController", description = "Controlador do recurso Cliente.")
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
            @Parameter(name = "CustomerRequest", description = "Estrutura de transporte de entrada de dados.", required = true)
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
            @Parameter(name = "CustomerRequest", description = "Estrutura de transporte de entrada de dados.", required = true)
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
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CustomerResponse.class))}
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
    public ResponseEntity<Page<CustomerAllResponse>> pageAll(
            @PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC, page = 0, size = 5) final Pageable paginacao) {

        var responsePage = customerPagePort.pageAll(paginacao);

        return ResponseEntity
                .ok()
                .body(responsePage);
    }
}
```

4. Opcional - Configurar endpoints customizados no application.yml;
```
springdoc:
  api-docs:
    enabled: true
    path: /v3/api-docs-users
  swagger-ui:
    enabled: true
    path: /swagger-ui-users/index.html
```

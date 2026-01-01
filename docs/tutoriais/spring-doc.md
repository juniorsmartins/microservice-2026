# TUTORIAL

## 1. Teoria

### Fontes:
- https://springdoc.org/#Introduction 
- https://github.com/springdoc/springdoc-openapi 
- https://www.baeldung.com/spring-rest-openapi-documentation 
- https://javadoc.io/doc/io.swagger.core.v3/swagger-annotations/latest/index.html 
- (Udemy) - https://www.udemy.com/course/microservices-do-0-a-gcp-com-spring-boot-kubernetes-e-docker/learn/lecture/51023705#overview 
- (Algaworks) - https://app.algaworks.com/aulas/4389/conhecendo-o-springdoc 

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

1. Adicionar dependências no build.gradle;
    a. Spring Doc;
    b. Spring Doc Hateoas (se estiver usando Spring Hateoas);
    c. Spring Doc Security.
2. Criar classe de configuração (OpenApiConfig);
3. Adicionar anotações nos Controllers, DTOs e etc;
4. Configurar no application.yml;
5. Acessar urls do swagger: localhost:9050/api-docs (Json) e localhost:9050/swagger-ui.html ;

5. Opcional - Configurar personalizações no application.yml;
3. Opcional - Adicionar "openapi" e "swagger-ui" nas configurações de exposição de endpoints do Actuator no application.yml;
5. Adicionar plugin Gradle ou Maven;

### Implementação: 

1. Adicionar dependência no build.gradle;
```
implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:3.0.0'
```

```

```

```

```


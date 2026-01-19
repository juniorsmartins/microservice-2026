# TUTORIAL

## 1. Teoria

### Fontes:
- https://java.testcontainers.org/ 
- https://java.testcontainers.org/quickstart/junit_5_quickstart/ 
- 
- 
- https://www.baeldung.com/spring-boot-built-in-testcontainers (dá para desenvolver com Testcontainers substituindo docker compose)
- https://www.baeldung.com/java-reuse-testcontainers 
- https://www.baeldung.com/spring-boot-redis-testcontainers 
- 

### Introdução: 
```

```

## 2. Configuração

### Passo-a-passo

1. Adicionar dependências no build.gradle:
    a. Testcontainers (testcontainers, testcontainers-junit-jupiter, testcontainers-mysql);
    b. Opcional - adicionar dependência 'com.redis:testcontainers-redis' para criar RedisContainer.
2. Configurar a classe de teste:
    a. Anotar a classe com @Testcontainers (ativa o suporte ao Testcontainers);
    b. Definir os containers necessários como estáticos com @Container;
    c. Anotar os containers com @ServiceConnection para não precisar definir @DynamicPropertySource (propriedades dinâmicas manualmente).
3. Opcional - montar testes de status para verificar containers em execução.


### Implementação:

```
testImplementation 'org.springframework.boot:spring-boot-testcontainers'
testImplementation 'org.testcontainers:testcontainers-junit-jupiter'
testImplementation 'org.testcontainers:testcontainers-mysql'
```

```
    @Container // Define o contêiner MySQL gerenciado pelo Testcontainers
    @ServiceConnection // Indica que esta é uma conexão de serviço para o Spring Boot. Substitui a necessidade de definir propriedades dinâmicas manualmente.
    static final MySQLContainer mysql = new MySQLContainer("mysql:lts-oraclelinux9")
            .withDatabaseName("db-news")
            .withUsername("mysql123")
            .withPassword("mysql123")
            .withEnv("MYSQL_SERVER_TIMEZONE", "UTC");

    @Container
    @ServiceConnection
    static final RedisContainer redis = new RedisContainer(DockerImageName.parse("redis:8.4.0"))
            .withExposedPorts(6379);
```

```
    @Test
    void dadoMySQLContainerConfigurado_quandoVerificarStatusDeExecucao_entaoStatusEstarAtivo() {
        Assertions.assertTrue(mysql.isRunning());
    }

    @Test
    void dadoRedisContainerConfigurado_quandoVerificarStatusDeExecuao_entaoStatusEstarAtivo() {
        Assertions.assertTrue(redis.isRunning());
    }
```






# TUTORIAL

## 1. Teoria

### Fontes:
- https://redis.io/pt/solucoes/casos-de-uso/cache/ 
- https://redis.io/docs/latest/develop/get-started/ 
- https://devdocs.io/redis/ 
- https://www.baeldung.com/spring-data-redis-tutorial 
- https://www.baeldung.com/spring-boot-redis-cache 
- https://spring.io/projects/spring-data-redis
- https://www.baeldung.com/spring-cache-tutorial 
- https://docs.spring.io/spring-data/redis/reference/redis.html 
- https://www.youtube.com/watch?v=YcI9b-lgi7w 
- https://www.youtube.com/watch?v=j65P_-yOX8g 
- https://github.com/margato/redis-spring-tutorial 
- https://medium.com/simform-engineering/spring-boot-caching-with-redis-1a36f719309f 

### Introdução: 
```
O armazenamento em cache melhora o tempo de resposta do aplicativo por manter cópias dos dados usados com mais 
frequência em um armazenamento efêmero, mas muito rápido. Se quiser atingir esses objetivos, recomendamos soluções 
de cache na memória, que mantêm o conjunto de trabalho em DRAM rápida, em vez de discos giratórios lentos, 
mostrando-se extremamente eficazes. O armazenamento em cache é comumente utilizado para melhorar a latência do 
aplicativo, mas um cache altamente disponível e resiliente também pode ajudar a dimensionar os aplicativos. Ao 
transferir responsabilidades da lógica do aplicativo principal para a camada de cache, liberamos recursos de 
computação para processar mais solicitações de entrada. 

Redis (REmote DIctionary Server) é um banco de dados NoSQL em memória que pode ser usado como banco de dados, cache 
e agente de mensagens. Ele suporta várias estruturas de dados, como strings, hashes, listas, conjuntos e conjuntos 
classificados. O Redis é conhecido por seu alto desempenho, escalabilidade e flexibilidade, tornando-o uma escolha 
popular para aplicativos que exigem acesso rápido a dados.

Estratégias: 

1) Pre-caching data (A API consulta o Redis já no startup/inicialização para carregar dados em memória);
2) On-demand caching (A API consulta o Redis quando necessário para carregar dados em memória);
    2.1) Read strategy - Cache Aside (Numa requisição, a API consulta o Redis antes de consultar o banco de dados); 
    2.2) Write Around: a API grava diretamente no banco de dados, ignorando o Redis;
    
    2.3) Write-Through - gravação: grava no banco e no cache. O cache fica entre o aplicativo e o armazenamento de 
    dados operacional, mas as atualizações são feitas de forma síncrona. Esse modelo de gravação favorece a 
    consistência dos dados entre o cache e o armazenamento de dados, pois a gravação é realizada no thread principal 
    do servidor. No entanto, isso pode impactar o desempenho de gravação, pois o aplicativo precisa aguardar a 
    confirmação de ambas as operações de gravação (no cache e no armazenamento de dados) antes de prosseguir;
    
    2.4) Write-Behind - escrita em segundo plano (Write-Back): os dados são inicialmente gravados no cache (por 
    exemplo, no Redis) e, em seguida, atualizados de forma assíncrona no armazenamento de dados operacional. Isso 
    permite melhorar o desempenho de gravação e facilita o desenvolvimento de aplicativos, pois o desenvolvedor 
    grava em único local (Redis). No entanto, esse modelo pode levar a inconsistências temporárias entre o cache e o 
    armazenamento de dados, especialmente em casos de falhas do sistema ou reinicializações inesperadas.

Estratégias para invalidação de cache:

1) Time-to-Live (TTL): definir um tempo de vida para cada entrada no cache. Após esse período, a entrada é automaticamente removida do cache.
2) Least Recently Used (LRU): remover as entradas menos recentemente usadas quando o cache atingir sua capacidade máxima.
3) Manual: o aplicativo pode explicitamente invalidar ou remover entradas do cache quando os dados subjacentes forem atualizados.

Padrao de chaves: 

<objeto>:<identificador>:<atributo>
Exemplo: user:12345:profile
```

## 2. Configuração


### Passo-a-passo

1. Adicionar dependências no build.gradle:
    a. Spring Data Redis (spring-boot-starter-data-redis);
    b. Spring Boot Starter Cache (spring-boot-starter-cache);
    c. Não esquecer das respectivas dependências de teste.
2. Configurar o application.yml:
    a. Configuração do Redis (host, port, password, etc);
    b. Configuração do Cache (estratégia de cache, TTL, etc);
    c. Configuração de Logging (nível de log para Redis).
3. Habilitar o cache na classe Main com @EnableCaching;
4. Implementar a lógica de cache nos serviços ou nos controllers:
    a. Usar anotações como @Cacheable, @CachePut e @CacheEvict para gerenciar o cache;
    b. Importante: o objeto de retorno do método precisa implementar Serializable (java.io.Serializable).
5. Criar o docker-compose.yml:
    a. Serviço do Redis (imagem oficial do Redis);
    b. Serviço do Redis-UI (opcional, para visualização dos dados no Redis).
6. Opcional - Configuração programática do RedisCacheManager (caso precise de uma configuração mais avançada):
    a. Criar uma classe de configuração que define o RedisCacheManager com políticas de expiração personalizadas.

Comandos para verificar o container do Redis:
```
docker exec -it redis sh 
redis-cli --raw
keys *
get <chave>
```

### Implementação: 

1. Adicionar dependências no build.gradle (coloquei no build do módulo application, pois as anotações estão nos serviços):
```
    implementation 'org.springframework.boot:spring-boot-starter-data-redis'
    testImplementation 'org.springframework.boot:spring-boot-starter-data-redis-test'
    implementation 'org.springframework.boot:spring-boot-starter-cache'
    testImplementation 'org.springframework.boot:spring-boot-starter-cache-test'
```

2. Configurar o application.yml:
```
spring:  
  data:
    redis:
      host: ${REDIS_HOST:redis}
      port: ${REDIS_PORT:6379}

  cache:
    type: redis 
    redis:
      time-to-live: 600000 
      cache-null-values: false 
    
logging:
  level:
    org.springframework.cache: DEBUG
    org.springframework.data.redis.cache: DEBUG
```

3. Habilitar o cache na classe Main com @EnableCaching;
```
@SpringBootApplication
@RefreshScope
@EnableCaching
public class ApiNewsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiNewsApplication.class, args);
	}
}
```

4. Implementar a lógica de cache nos serviços ou nos controllers (optei por colocar anotações nos serviços/UseCase):
```
    @Caching(
            put = {
                    @CachePut(value = "newsById", key = "#result.id")
            },
            evict = {
                    @CacheEvict(value = "newsPageAll", allEntries = true) // Limpa cache de paginação
            }
    )
    @Override
    public NewsDto create(NewsDto dto) {
        return newsSaveOutputPort.save(dto);
    }
    
    @Caching(
            put = {
                    @CachePut(value = "newsById", key = "#result.id"),
            },
            evict = {
                    @CacheEvict(cacheNames = {"newsPageAll"}, allEntries = true)
            }
    )
    @Override
    public NewsDto update(NewsDto dto) {

        return newsFindByIdOutputPort.findById(dto.id())
                .map(news -> newsSaveOutputPort.save(dto))
                .orElseThrow(() -> new RuntimeException("News not found with id: " + dto.id()));
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "newsById", key = "#id"),
                    @CacheEvict(cacheNames = {"newsPageAll"}, allEntries = true)
            }
    )
    @Override
    public void deleteById(UUID id) {

        newsFindByIdOutputPort.findById(id)
                .ifPresentOrElse(news -> newsDeleteByIdOutputPort.deleteById(news.id()),
                        () -> {
                            throw new IllegalArgumentException("News with id " + id + " not found");
                        }
                );

    }

    @Cacheable(value = "newsById", key = "#id")
    @Override
    public NewsDto findById(final UUID id) {

        return newsFindByIdOutputPort.findById(id)
                .orElseThrow(() -> new RuntimeException("News with id " + id + " not found"));
    }

    @Cacheable(value = "newsPageAll", key = "#pageable", condition = "#pageable.pageNumber <= 1", unless = "#result == null || #result.isEmpty()")
    @Override
    public Page<NewsDto> pageAll(Pageable pageable) {
        return newsPageAllOutputPort.pageAll(pageable);
    }
```
Objeto de retorno com Serializable (java.io.Serializable)
```
public record NewsDto(

        UUID id,
        String hat,
        String title,
        String thinLine,
        String text,
        String author,
        String font
        
) implements Serializable {
}
```

5. Criar o docker-compose.yml:
Serviço de Redis:
```
  redis:
    image: redis:8.4.0
    container_name: redis
    hostname: redis
    ports:
      - "6379:6379"
    deploy:
      resources:
        limits:
          cpus: '1.0'
          memory: 1024M
    restart: unless-stopped
    environment:
      TZ: utc
      REDIS_HOST: redis
      REDIS_PORT: 6379
    volumes:
      - redis-data:/data
    command: ["redis-server", "--appendonly", "yes"]
    healthcheck:
      test: [ "CMD", "redis-cli", "ping" ]
      interval: 10s
      timeout: 5s
      retries: 5
    networks:
      - communication
```
UI do Redis (opcional):)
```
  redis-insight:
    image: redis/redisinsight:latest
    container_name: redis-insight
    hostname: redis-insight
    ports:
      - "5540:5540"
    restart: unless-stopped
    depends_on:
      redis:
        condition: service_healthy
```
Volume:
```
volumes:
  redis-data:
    name: redis-data
```



package backend.core.api_news.controllers;

import backend.core.api_news.dtos.requests.NewsRequest;
import backend.core.api_news.dtos.responses.NewsCreateResponse;
import backend.core.api_news.repositories.NewsRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mysql.MySQLContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers // Ativa Testcontainers no JUnit
class NewsControllerIntegrationTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    NewsRepository newsRepository;

    RestTestClient restTestClient;

    @Container // Define o contêiner MySQL gerenciado pelo Testcontainers
    static MySQLContainer mysql = new MySQLContainer("mysql:lts-oraclelinux9")
            .withDatabaseName("db-news")
            .withUsername("mysql123")
            .withPassword("mysql123")
            .withEnv("MYSQL_SERVER_TIMEZONE", "UTC");

    @DynamicPropertySource // Configura propriedades dinâmicas para o Spring Boot usar o contêiner MySQL
    static void mysqlProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.datasource.url", mysql::getJdbcUrl);
        dynamicPropertyRegistry.add("spring.datasource.username", mysql::getUsername);
        dynamicPropertyRegistry.add("spring.datasource.password", mysql::getPassword);
    }

    @BeforeEach
    void setUp() {
        restTestClient = RestTestClient.bindToApplicationContext(webApplicationContext).build();
    }

    @Nested
    @DisplayName("CreateIntegrationValid")
    class CreateIntegrationValid {

        @Test
        void dadaRequisicaoValida_quandoCriarNoticia_entaoRetornarHttp201AndDadosValidos() {
            var newsRequest = new NewsRequest("Tênis", "Djokovic vence mais uma",
                    "Próximo desafio será contra Nadal", "Texto da matéria",
                    "Tom Wolfe", "Tênis Global");

            restTestClient.post()
                    .uri("/api/v1.0/news")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(newsRequest)
                    .exchange()
                    .expectStatus().isCreated()
                    .expectBody()
                    .jsonPath("$.id").exists()
                    .jsonPath("$.hat").isEqualTo("Tênis")
                    .jsonPath("$.title").isEqualTo("Djokovic vence mais uma")
                    .jsonPath("$.thinLine").isEqualTo("Próximo desafio será contra Nadal")
                    .jsonPath("$.text").isEqualTo("Texto da matéria")
                    .jsonPath("$.author").isEqualTo("Tom Wolfe")
                    .jsonPath("$.font").isEqualTo("Tênis Global");
        }

        @Test
        void dadaRequisicaoValida_quandoCriarNoticia_entaoSalvarDadosNoBanco() {
            var newsRequest = new NewsRequest("Boxe", "Canelo vence mais uma",
                    "Próxima luta será contra Terence", "Texto da matéria",
                    "Tom Wolfe", "Tênis Global");

            var response = restTestClient.post()
                    .uri("/api/v1.0/news")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(newsRequest)
                    .exchange()
                    .expectStatus().isCreated()
                    .expectBody(NewsCreateResponse.class)
                    .returnResult()
                    .getResponseBody();

            var newsId = response.id();
            var newsEntity = newsRepository.findById(newsId).orElseThrow();
            Assertions.assertEquals(newsRequest.hat(), newsEntity.getHat());
            Assertions.assertEquals(newsRequest.title(), newsEntity.getTitle());
            Assertions.assertEquals(newsRequest.thinLine(), newsEntity.getThinLine());
            Assertions.assertEquals(newsRequest.text(), newsEntity.getText());
            Assertions.assertEquals(newsRequest.author(), newsEntity.getAuthor());
            Assertions.assertEquals(newsRequest.font(), newsEntity.getFont());
        }
    }
}
package backend.core.api_news.controllers;

import backend.core.api_news.dtos.requests.NewsRequest;
import backend.core.api_news.dtos.responses.NewsCreateResponse;
import backend.core.api_news.repositories.NewsRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.web.context.WebApplicationContext;

@Tag("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NewsControllerIntegrationTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    NewsRepository newsRepository;

    RestTestClient restTestClient;

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
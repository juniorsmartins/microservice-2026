package backend.core.api_news.controllers;

import backend.core.api_news.dtos.requests.NewsCreateRequest;
import backend.core.api_news.dtos.responses.NewsCreateResponse;
import backend.core.api_news.dtos.responses.NewsFindByIdResponse;
import backend.core.api_news.dtos.responses.NewsResponse;
import backend.core.api_news.dtos.responses.NewsUpdateResponse;
import backend.core.api_news.entities.NewsEntity;
import backend.core.api_news.repositories.NewsRepository;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
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

    @AfterEach
    void tearDown() {
        newsRepository.deleteAll();
    }

    @Nested
    @DisplayName("CreateIntegrationValid")
    class CreateIntegrationValid {

        @Test
        void dadaRequisicaoValida_quandoCriarNoticia_entaoRetornarHttp201AndDadosValidos() {
            var newsRequest = new NewsCreateRequest("Tênis", "Djokovic vence mais uma",
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
            var newsRequest = new NewsCreateRequest("Boxe", "Canelo vence mais uma",
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

    @Nested
    @DisplayName("UpdateIntegrationValid")
    class UpdateIntegrationValid {

        @Test
        void dadaRequisicaoValida_quandoAtualizarNoticia_entaoRetornarHttp200AndDadosValidos() {
            var newsEntity = new NewsEntity(null, "Tênis", "Djokovic vence mais uma",
                    "Próximo desafio será contra Nadal", "Texto da matéria",
                    "Tom Wolfe", "Tênis Global");
            newsRepository.save(newsEntity);
            var newsId = newsEntity.getId();

            var newsRequest = new NewsCreateRequest("Tênis Atual", "Djokovic vence mais uma Atual",
                    "Próximo desafio será contra Nadal Atual", "Texto da matéria Atual",
                    "Tom Wolfe Atual", "Tênis Global Atual");

            restTestClient.put()
                    .uri("/api/v1.0/news/{id}", newsId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(newsRequest)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$.id").exists()
                    .jsonPath("$.hat").isEqualTo("Tênis Atual")
                    .jsonPath("$.title").isEqualTo("Djokovic vence mais uma Atual")
                    .jsonPath("$.thinLine").isEqualTo("Próximo desafio será contra Nadal Atual")
                    .jsonPath("$.text").isEqualTo("Texto da matéria Atual")
                    .jsonPath("$.author").isEqualTo("Tom Wolfe Atual")
                    .jsonPath("$.font").isEqualTo("Tênis Global Atual");
        }

        @Test
        void dadaRequisicaoValida_quandoAtualizarNoticia_entaoSalvarDadosNoBanco() {
            var newsEntity = new NewsEntity(null, "Tênis", "Djokovic vence mais uma",
                    "Próximo desafio será contra Nadal", "Texto da matéria",
                    "Tom Wolfe", "Tênis Global");
            newsRepository.save(newsEntity);
            var newsId = newsEntity.getId();

            var newsRequest = new NewsCreateRequest("Tênis Atual", "Djokovic vence mais uma Atual",
                    "Próximo desafio será contra Nadal Atual", "Texto da matéria Atual",
                    "Tom Wolfe Atual", "Tênis Global Atual");

            var response = restTestClient.put()
                    .uri("/api/v1.0/news/{id}", newsId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(newsRequest)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(NewsUpdateResponse.class)
                    .returnResult()
                    .getResponseBody();

            var newsDoBanco = newsRepository.findById(newsId).orElseThrow();
            Assertions.assertEquals(newsDoBanco.getHat(), response.hat());
            Assertions.assertEquals(newsDoBanco.getTitle(), response.title());
            Assertions.assertEquals(newsDoBanco.getThinLine(), response.thinLine());
            Assertions.assertEquals(newsDoBanco.getText(), response.text());
            Assertions.assertEquals(newsDoBanco.getAuthor(), response.author());
            Assertions.assertEquals(newsDoBanco.getFont(), response.font());
            Assertions.assertNotNull(newsDoBanco.getCreatedDate());
            Assertions.assertNotNull(newsDoBanco.getLastModifiedDate());

            Assertions.assertEquals(newsDoBanco.getHat(), newsRequest.hat());
            Assertions.assertEquals(newsDoBanco.getTitle(), newsRequest.title());
            Assertions.assertEquals(newsDoBanco.getThinLine(), newsRequest.thinLine());
            Assertions.assertEquals(newsDoBanco.getText(), newsRequest.text());
            Assertions.assertEquals(newsDoBanco.getAuthor(), newsRequest.author());
            Assertions.assertEquals(newsDoBanco.getFont(), newsRequest.font());
        }
    }

    @Nested
    @DisplayName("DeleteByIdIntegrationValid")
    class DeleteByIdIntegrationValid {

        @Test
        void dadaRequisicaoValida_quandoDeletarNoticiaPorId_entaoRetornarHttp204NoContentAndApagarDoBanco() {
            var newsEntity = new NewsEntity(null, "Tênis", "Djokovic vence mais uma",
                    "Próximo desafio será contra Nadal", "Texto da matéria",
                    "Tom Wolfe", "Tênis Global");
            newsRepository.save(newsEntity);
            var newsId = newsEntity.getId();

            restTestClient.delete()
                    .uri("/api/v1.0/news/{id}", newsId)
                    .exchange()
                    .expectStatus().isNoContent();

            var newsDoBanco = newsRepository.findById(newsId);
            Assertions.assertTrue(newsDoBanco.isEmpty());
        }
    }

    @Nested
    @DisplayName("FindByIdIntegrationValid")
    class FindByIdIntegrationValid {

        @Test
        void dadaRequisicaoValida_quandoConsultarNoticiaPorId_entaoRetornarHttp200AndDadosValidos() {
            var newsEntity = new NewsEntity(null, "Tênis", "Djokovic vence mais uma",
                    "Próximo desafio será contra Nadal", "Texto da matéria",
                    "Tom Wolfe", "Tênis Global");
            newsRepository.save(newsEntity);
            var newsId = newsEntity.getId();

            restTestClient.get()
                    .uri("/api/v1.0/news/{id}", newsId)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$.id").isEqualTo(newsId.toString())
                    .jsonPath("$.hat").isEqualTo("Tênis")
                    .jsonPath("$.title").isEqualTo("Djokovic vence mais uma")
                    .jsonPath("$.thinLine").isEqualTo("Próximo desafio será contra Nadal")
                    .jsonPath("$.text").isEqualTo("Texto da matéria")
                    .jsonPath("$.author").isEqualTo("Tom Wolfe")
                    .jsonPath("$.font").isEqualTo("Tênis Global");
        }
    }
}
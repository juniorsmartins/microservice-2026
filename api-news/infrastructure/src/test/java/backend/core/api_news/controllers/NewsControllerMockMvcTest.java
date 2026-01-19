package backend.core.api_news.controllers;

import backend.core.api_news.configs.NoCacheTestConfig;
import backend.core.api_news.dtos.ContactInfoDto;
import backend.core.api_news.dtos.NewsDto;
import backend.core.api_news.dtos.requests.NewsRequest;
import backend.core.api_news.dtos.responses.NewsCreateResponse;
import backend.core.api_news.dtos.responses.NewsResponse;
import backend.core.api_news.gateways.NewsQueryPort;
import backend.core.api_news.ports.input.NewsCreateInputPort;
import backend.core.api_news.ports.input.NewsDeleteByIdInputPort;
import backend.core.api_news.ports.input.NewsFindByIdInputPort;
import backend.core.api_news.ports.input.NewsUpdateInputPort;
import backend.core.api_news.presenters.NewsPresenterPort;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.util.UUID;

@Tag("unit")
@WebMvcTest(NewsController.class)
@Import(NoCacheTestConfig.class)  // Importa configuração que fornece CacheManager fake
class NewsControllerMockMvcTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    NewsCreateInputPort newsCreateInputPort;

    @MockitoBean
    NewsQueryPort newsQueryPort;

    @MockitoBean
    NewsPresenterPort newsPresenterPort;

    @MockitoBean
    ContactInfoDto contactInfoDto;

    @MockitoBean
    NewsDeleteByIdInputPort newsDeleteByIdInputPort;

    @MockitoBean
    NewsFindByIdInputPort newsFindByIdInputPort;

    @MockitoBean
    NewsUpdateInputPort newsUpdateInputPort;

    RestTestClient restTestClient;

    @BeforeEach
    void setUp() {
        restTestClient = RestTestClient.bindTo(mockMvc).build();
    }

    @Nested
    @DisplayName("CreateValid")
    class CreateValid {

        @Test
        void dadaRequisicaoValida_quandoCriarNoticia_entaoRetornarHttp201AndDadosValidos() {
            var newsRequest = new NewsRequest("Tênis", "Djokovic vence mais uma",
                    "Próximo desafio será contra Nadal", "Texto da matéria",
                    "Tom Wolfe", "Tênis Global");

            var newsDto = new NewsDto(UUID.randomUUID(), "Tênis",
                    "Djokovic vence mais uma", "Próximo desafio será contra Nadal",
                    "Texto da matéria", "Tom Wolfe", "Tênis Global");

            var newsResponse = new NewsCreateResponse(UUID.randomUUID(), "Tênis",
                    "Djokovic vence mais uma", "Próximo desafio será contra Nadal",
                    "Texto da matéria", "Tom Wolfe", "Tênis Global");

            Mockito.when(newsPresenterPort.toNewsDto(newsRequest)).thenReturn(newsDto);
            Mockito.when(newsCreateInputPort.create(newsDto)).thenReturn(newsDto);
            Mockito.when(newsPresenterPort.toNewsCreateResponse(newsDto)).thenReturn(newsResponse);

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

            Mockito.verify(newsPresenterPort).toNewsDto(newsRequest);
            Mockito.verify(newsCreateInputPort).create(newsDto);
            Mockito.verify(newsPresenterPort).toNewsCreateResponse(newsDto);
        }
    }

    @Nested
    @DisplayName("UpdateValid")
    class UpdateValid {

        @Test
        void dadaRequisicaoValida_quandoAtualizarNoticia_entaoRetornarHttp200AndDadosValidos() {
            var newsId = UUID.randomUUID();

            var newsRequest = new NewsRequest("Tênis", "Djokovic vence mais uma",
                    "Próximo desafio será contra Nadal", "Texto da matéria",
                    "Tom Wolfe", "Tênis Global");

            var newsDto = new NewsDto(newsId, "Tênis", "Djokovic vence mais uma",
                    "Próximo desafio será contra Nadal", "Texto da matéria", "Tom Wolfe",
                    "Tênis Global");

            var newsResponse = new NewsResponse(newsId, "Tênis", "Djokovic vence mais uma",
                    "Próximo desafio será contra Nadal", "Texto da matéria", "Tom Wolfe",
                    "Tênis Global");

            Mockito.when(newsPresenterPort.toNewsDto(newsId, newsRequest)).thenReturn(newsDto);
            Mockito.when(newsUpdateInputPort.update(newsDto)).thenReturn(newsDto);
            Mockito.when(newsPresenterPort.toNewsResponse(newsDto)).thenReturn(newsResponse);

            restTestClient.put()
                    .uri("/api/v1.0/news/{id}", newsId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(newsRequest)
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

            Mockito.verify(newsPresenterPort).toNewsDto(newsId, newsRequest);
            Mockito.verify(newsUpdateInputPort).update(newsDto);
            Mockito.verify(newsPresenterPort).toNewsResponse(newsDto);
        }
    }

    @Nested
    @DisplayName("FindByIdValid")
    class FindByIdValid {

        @Test
        void dadaRequisicaoValida_quandoBuscarNoticiaPorId_entaoRetornarHttp200AndDadosValidos() {
            var newsId = UUID.randomUUID();

            var newsDto = new NewsDto(newsId, "Tênis", "Djokovic vence mais uma",
                    "Próximo desafio será contra Nadal", "Texto da matéria", "Tom Wolfe",
                    "Tênis Global");

            var newsResponse = new NewsResponse(newsId, "Tênis", "Djokovic vence mais uma",
                    "Próximo desafio será contra Nadal", "Texto da matéria", "Tom Wolfe",
                    "Tênis Global");

            Mockito.when(newsFindByIdInputPort.findById(newsId)).thenReturn(newsDto);
            Mockito.when(newsPresenterPort.toNewsResponse(newsDto)).thenReturn(newsResponse);

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

            Mockito.verify(newsFindByIdInputPort).findById(newsId);
            Mockito.verify(newsPresenterPort).toNewsResponse(newsDto);
        }
    }

    @Nested
    @DisplayName("DeleteByIdValid")
    class DeleteByIdValid {

        @Test
        void dadaRequisicaoValida_quandoDeletarNoticia_entaoRetornarHttp204() {
            var newsId = UUID.randomUUID();

            Mockito.doNothing().when(newsDeleteByIdInputPort).deleteById(newsId);

            restTestClient.delete()
                    .uri("/api/v1.0/news/{id}", newsId)
                    .exchange()
                    .expectStatus().isNoContent();

            Mockito.verify(newsDeleteByIdInputPort).deleteById(newsId);
        }
    }
}

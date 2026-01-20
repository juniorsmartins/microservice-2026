package backend.core.api_news.controllers;

import backend.core.api_news.configs.NoCacheTestConfig;
import backend.core.api_news.dtos.NewsDto;
import backend.core.api_news.dtos.requests.NewsRequest;
import backend.core.api_news.dtos.responses.NewsCreateResponse;
import backend.core.api_news.dtos.responses.NewsResponse;
import backend.core.api_news.ports.input.*;
import backend.core.api_news.presenters.NewsPresenterPort;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.util.List;
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
    NewsPresenterPort newsPresenterPort;

    @MockitoBean
    NewsDeleteByIdInputPort newsDeleteByIdInputPort;

    @MockitoBean
    NewsFindByIdInputPort newsFindByIdInputPort;

    @MockitoBean
    NewsUpdateInputPort newsUpdateInputPort;

    @MockitoBean
    NewsPageAllInputPort newsPageAllInputPort;

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
            var newsId = UUID.randomUUID();

            var newsRequest = new NewsRequest("Tênis", "Djokovic vence mais uma",
                    "Próximo desafio será contra Nadal", "Texto da matéria",
                    "Tom Wolfe", "Tênis Global");

            var newsDto = new NewsDto(newsId, "Tênis",
                    "Djokovic vence mais uma", "Próximo desafio será contra Nadal",
                    "Texto da matéria", "Tom Wolfe", "Tênis Global");

            var newsResponse = new NewsCreateResponse(newsId, "Tênis",
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
    @DisplayName("PageAllValid")
    class PageAllValid {

        @Test
        void dadaRequisicaoValida_quandoBuscarTodosPaginados_entaoRetornarHttp200AndQuantidadeValida() {
            var newsId1 = UUID.randomUUID();
            var newsId2 = UUID.randomUUID();

            var newsDto1 = new NewsDto(newsId1, "Tênis",
                    "Djokovic vence mais uma", "Próximo desafio será contra Nadal",
                    "Texto da matéria", "Tom Wolfe", "Tênis Global");

            var newsDto2 = new NewsDto(newsId2, "Futebol Americano",
                    "Dallas Cowboys vence Minesota", "Próximo desafio será contra Bears",
                    "Texto da matéria", "Gay Talese", "NFL");

            var newsResponse1 = new NewsResponse(newsId1, "Tênis",
                    "Djokovic vence mais uma", "Próximo desafio será contra Nadal",
                    "Texto da matéria", "Tom Wolfe", "Tênis Global");

            var newsResponse2 = new NewsResponse(newsId2, "Futebol Americano",
                    "Dallas Cowboys vence Minesota", "Próximo desafio será contra Bears",
                    "Texto da matéria", "Gay Talese", "NFL");

            Page<NewsDto> paginaDto = new PageImpl<>(List.of(newsDto1, newsDto2));
            Mockito.when(newsPageAllInputPort.pageAll(Mockito.any(Pageable.class))).thenReturn(paginaDto);
            Mockito.when(newsPresenterPort.toNewsResponse(newsDto1)).thenReturn(newsResponse1);
            Mockito.when(newsPresenterPort.toNewsResponse(newsDto2)).thenReturn(newsResponse2);

            restTestClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/1.0/news")
                            .queryParam("page", 0)
                            .queryParam("size", 10)
                            .build())
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$.content").isArray()
                    .jsonPath("$.content.length()").isEqualTo(2)
                    .jsonPath("$.content[0].id").isEqualTo(newsId1.toString())
                    .jsonPath("$.content[0].hat").isEqualTo("Tênis")
                    .jsonPath("$.content[1].hat").isEqualTo("Futebol Americano")
                    .jsonPath("$.totalElements").isEqualTo(2)
                    .jsonPath("$.size").isEqualTo(2);
        }
    }
}

package backend.core.api_news.controllers;

import backend.core.api_news.configs.NoCacheTestConfig;
import backend.core.api_news.dtos.ContactInfoDto;
import backend.core.api_news.dtos.NewsDto;
import backend.core.api_news.dtos.requests.NewsRequest;
import backend.core.api_news.dtos.responses.NewsCreateResponse;
import backend.core.api_news.gateways.NewsQueryPort;
import backend.core.api_news.ports.input.NewsCreateInputPort;
import backend.core.api_news.ports.input.NewsDeleteByIdInputPort;
import backend.core.api_news.presenters.NewsPresenterPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.util.UUID;

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
        }
    }
}

package backend.core.api_news.controllers;

import backend.core.api_news.configs.NoCacheTestConfig;
import backend.core.api_news.dtos.ContactInfoDto;
import backend.core.api_news.gateways.NewsQueryPort;
import backend.core.api_news.ports.input.NewsCreateInputPort;
import backend.core.api_news.ports.input.NewsDeleteByIdInputPort;
import backend.core.api_news.presenters.NewsPresenterPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.client.RestTestClient;

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

        }
    }
}
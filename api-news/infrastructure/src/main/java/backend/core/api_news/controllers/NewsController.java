package backend.core.api_news.controllers;

import backend.core.api_news.dtos.ContactInfoDto;
import backend.core.api_news.dtos.requests.NewsRequest;
import backend.core.api_news.dtos.responses.NewsCreateResponse;
import backend.core.api_news.dtos.responses.NewsResponse;
import backend.core.api_news.gateways.NewsQueryPort;
import backend.core.api_news.ports.input.NewsCreateInputPort;
import backend.core.api_news.ports.input.NewsDeleteByIdInputPort;
import backend.core.api_news.ports.input.NewsFindByIdInputPort;
import backend.core.api_news.presenters.NewsPresenterPort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Tag(name = "News", description = "Controlador do recurso Notícias.")
@Slf4j
@NullMarked
@RestController
@RequestMapping(path = {"/api/"})
@RequiredArgsConstructor
public class NewsController {

    private final NewsCreateInputPort newsCreateInputPort;

    private final NewsQueryPort newsQueryPort;

    private final NewsPresenterPort newsPresenterPort;

    private final ContactInfoDto contactInfoDto;

    private final NewsDeleteByIdInputPort newsDeleteByIdInputPort;

    private final NewsFindByIdInputPort newsFindByIdInputPort;

    @GetMapping(value = "/{version}/news/hostcheck", version = "1.0")
    public String checkHost() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostName() + " - " + InetAddress.getLocalHost().getHostAddress();
    }

    @GetMapping(value = "/{version}/news/contact-info", version = "1.0")
    public String contactInfo() {
        return contactInfoDto.toString();
    }

    @Operation(summary = "Cadastrar", description = "Recurso para criar novas notícias.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Created - recurso cadastrado com sucesso.",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = NewsCreateResponse.class))}
                    ),
                    @ApiResponse(responseCode = "400", description = "Bad Request - requisição mal formulada.",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}
                    ),
                    @ApiResponse(responseCode = "409", description = "Conflict - violação de regras de negócio.",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}
                    ),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error - situação inesperada no servidor.",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}
                    )
            }
    )
    @PostMapping(value = "/{version}/news", version = "1.0")
//    @CachePut(value = "createNews", key = "#result.body().id()") // Atualiza o cache após a criação de uma nova notícia. A chave do cache é o ID da notícia criada.
    public ResponseEntity<NewsCreateResponse> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Estrutura de transporte para entrada de dados.", required = true)
            @RequestBody NewsRequest request) {

        var response = Optional.of(request)
                .map(newsPresenterPort::toNewsDto)
                .map(newsCreateInputPort::create)
                .map(newsPresenterPort::toNewsCreateResponse)
                .orElseThrow();

        return ResponseEntity
                .created(URI.create("/api/1.0/news/" + response.id()))
                .body(response);
    }

    @PutMapping(value = "/{version}/news/{id}", version = "1.0")
    public ResponseEntity<NewsResponse> update(
            @PathVariable(name = "id") final UUID id,
            @RequestBody NewsRequest request) {

        return ResponseEntity
                .ok()
                .build();
    }

    @GetMapping(value = "/{version}/news/{id}", version = "1.0")
    public ResponseEntity<NewsResponse> findById(@PathVariable(name = "id") final UUID id) {

        var newsDto = newsFindByIdInputPort.findById(id);
        var response = newsPresenterPort.toNewsResponse(newsDto);

        return ResponseEntity
                .ok()
                .body(response);
    }

    @DeleteMapping(value = "/{version}/news/{id}", version = "1.0")
    public ResponseEntity<Void> deleteById(@PathVariable(name = "id") final UUID id) {

        newsDeleteByIdInputPort.deleteById(id);

        return ResponseEntity
                .noContent()
                .build();
    }

    @GetMapping(value = "/{version}/news", version = "1.0")
    @Cacheable(value = "newsByTitle", key = "#title", unless = "#result == null || #result.isEmpty()")
    // Cache com base no título. Não armazena resultados nulos ou vazios.
    public List<NewsResponse> findByTitleLike(@RequestParam(name = "title") String title) {

        log.info("\n\n 1.0 \n\n");
        return newsQueryPort.findByTitleLike(title)
                .stream()
                .map(newsPresenterPort::toNewsResponse)
                .toList();
    }

    @GetMapping(value = "/{version}/news", version = "2.0")
    public List<NewsResponse> findByTitleLikeV2(@RequestParam(name = "title") String title) {

        log.info("\n\n 2.0 \n\n");
        return newsQueryPort.findByTitleLike(title)
                .stream()
                .map(newsPresenterPort::toNewsResponse)
                .toList();
    }
}


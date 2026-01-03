package backend.core.api_news.controllers;

import backend.core.api_news.dtos.requests.NewsCreateRequest;
import backend.core.api_news.dtos.responses.NewsCreateResponse;
import backend.core.api_news.dtos.responses.NewsResponse;
import backend.core.api_news.ports.input.NewsCreateInputPort;
import backend.core.api_news.gateways.NewsQueryPort;
import backend.core.api_news.presenters.NewsPresenterPort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Tag(name = "NewsController", description = "Controlador do recurso Notícias.")
@Slf4j
@NullMarked
@RestController
@RequestMapping(path = {"/api/"})
@RequiredArgsConstructor
public class NewsController {

    private final NewsCreateInputPort newsCreateInputPort;

    private final NewsQueryPort newsQueryPort;

    private final NewsPresenterPort newsPresenterPort;

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
    public ResponseEntity<NewsCreateResponse> create(
            @Parameter(name = "NewsCreateRequest", description = "Estrutura de transporte de entrada de dados.", required = true)
            @RequestBody NewsCreateRequest request) {

        var response = Optional.of(request)
                .map(newsPresenterPort::toNewsDto)
                .map(newsCreateInputPort::create)
                .map(newsPresenterPort::toNewsCreateResponse)
                .orElseThrow();

        return ResponseEntity
                .created(URI.create("/api/1.0/news/" + response.id()))
                .body(response);
    }

    @GetMapping(value = "/{version}/news", version = "1.0")
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

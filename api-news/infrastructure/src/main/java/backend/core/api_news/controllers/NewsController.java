package backend.core.api_news.controllers;

import backend.core.api_news.dtos.requests.NewsRequest;
import backend.core.api_news.dtos.responses.NewsCreateResponse;
import backend.core.api_news.dtos.responses.NewsResponse;
import backend.core.api_news.ports.input.NewsCreateInputPort;
import backend.core.api_news.ports.input.NewsDeleteByIdInputPort;
import backend.core.api_news.ports.input.NewsFindByIdInputPort;
import backend.core.api_news.ports.input.NewsUpdateInputPort;
import backend.core.api_news.presenters.NewsPresenterPort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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

    private final NewsPresenterPort newsPresenterPort;

    private final NewsDeleteByIdInputPort newsDeleteByIdInputPort;

    private final NewsFindByIdInputPort newsFindByIdInputPort;

    private final NewsUpdateInputPort newsUpdateInputPort;

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
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Estrutura de transporte para entrada de dados.", required = true)
            @RequestBody @Valid NewsRequest request) {

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
            @RequestBody @Valid NewsRequest request) {

        var newsDto = newsPresenterPort.toNewsDto(id, request);
        var newsUpdate = newsUpdateInputPort.update(newsDto);
        var response = newsPresenterPort.toNewsResponse(newsUpdate);

        return ResponseEntity
                .ok()
                .body(response);
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
}


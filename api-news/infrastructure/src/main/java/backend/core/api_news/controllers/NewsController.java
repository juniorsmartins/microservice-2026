package backend.core.api_news.controllers;

import backend.core.api_news.annotations.PageableParameter;
import backend.core.api_news.dtos.requests.NewsRequest;
import backend.core.api_news.dtos.responses.NewsCreateResponse;
import backend.core.api_news.dtos.responses.NewsResponse;
import backend.core.api_news.dtos.responses.NewsUpdateResponse;
import backend.core.api_news.ports.input.*;
import backend.core.api_news.presenters.NewsPresenterPort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.resilience.annotation.Retryable;
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

    private final NewsPageAllInputPort newsPageAllInputPort;

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

    @Operation(summary = "Atualizar", description = "Recurso para atualizar clientes.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK - requisição bem sucedida e com retorno.",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = NewsUpdateResponse.class))}
                    ),
                    @ApiResponse(responseCode = "400", description = "Bad Request - requisição mal formulada.",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}
                    ),
                    @ApiResponse(responseCode = "404", description = "Not Found - recurso não encontrado.",
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
    @PutMapping(value = "/{version}/news/{id}", version = "1.0")
    public ResponseEntity<NewsUpdateResponse> update(
            @Parameter(name = "id", description = "Identificador único do recurso.", example = "034eb74c-69ee-4bd4-a064-5c4cc5e9e748", required = true)
            @PathVariable(name = "id") final UUID id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Estrutura de transporte para entrada de dados.", required = true)
            @RequestBody @Valid NewsRequest request) {

        var newsDto = newsPresenterPort.toNewsDto(id, request);
        var newsUpdate = newsUpdateInputPort.update(newsDto);
        var response = newsPresenterPort.toNewsUpdateResponse(newsUpdate);

        return ResponseEntity
                .ok()
                .body(response);
    }

    @Operation(summary = "Desativar", description = "Recurso para desativar clientes.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "No Content - requisição bem sucedida e sem retorno.",
                            content = {@Content(mediaType = "application/json")}
                    ),
                    @ApiResponse(responseCode = "400", description = "Bad Request - requisição mal formulada.",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}
                    ),
                    @ApiResponse(responseCode = "404", description = "Not Found - recurso não encontrado.",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}
                    ),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error - situação inesperada no servidor.",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}
                    )
            }
    )
    @DeleteMapping(value = "/{version}/news/{id}", version = "1.0")
    public ResponseEntity<Void> deleteById(
            @Parameter(name = "id", description = "Identificador único do recurso.", example = "034eb74c-69ee-4bd4-a064-5c4cc5e9e748", required = true)
            @PathVariable(name = "id") final UUID id) {

        newsDeleteByIdInputPort.deleteById(id);

        return ResponseEntity
                .noContent()
                .build();
    }

    @Operation(summary = "Consultar por ID", description = "Recurso para consultar clientes por ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK - requisição bem sucedida e com retorno.",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = NewsResponse.class))}
                    ),
                    @ApiResponse(responseCode = "400", description = "Bad Request - requisição mal formulada.",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}
                    ),
                    @ApiResponse(responseCode = "404", description = "Not Found - recurso não encontrado.",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}
                    ),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error - situação inesperada no servidor.",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}
                    )
            }
    )
    @GetMapping(value = "/{version}/news/{id}", version = "1.0")
    public ResponseEntity<NewsResponse> findById(@PathVariable(name = "id") final UUID id) {

        var newsDto = newsFindByIdInputPort.findById(id);
        var response = newsPresenterPort.toNewsResponse(newsDto);

        return ResponseEntity
                .ok()
                .body(response);
    }

    @Operation(summary = "Paginar todos", description = "Recurso para buscar todos os clientes paginados.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK - requisição bem sucedida e com retorno.",
                            content = {@Content(mediaType = "application/json", array = @ArraySchema(minItems = 0,
                                    schema = @Schema(implementation = NewsResponse.class)))
                            }
                    ),
                    @ApiResponse(responseCode = "400", description = "Bad Request - requisição mal formulada.",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}
                    ),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error - situação inesperada no servidor.",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}
                    )
            }
    )
    @GetMapping(value = "/{version}/news", version = "1.0")
    @Retryable(
            maxRetries = 4,
            jitter = 10,
            delay = 1000,
            multiplier = 2
    )
    @PageableParameter
    public ResponseEntity<Page<NewsResponse>> pageAll(
            @Parameter(hidden = true)
            @PageableDefault(sort = "title", direction = Sort.Direction.ASC, page = 0, size = 5) final Pageable paginacao) {

        var responsePage = newsPageAllInputPort.pageAll(paginacao)
                .map(newsPresenterPort::toNewsResponse);

        return ResponseEntity
                .ok()
                .body(responsePage);
    }
}


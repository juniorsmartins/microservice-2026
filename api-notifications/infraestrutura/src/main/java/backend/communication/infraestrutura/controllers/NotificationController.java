package backend.communication.infraestrutura.controllers;

import backend.communication.infraestrutura.anotations.PageableParameter;
import backend.communication.infraestrutura.dtos.responses.NotificationResponse;
import backend.communication.infraestrutura.gateways.NotificationPagePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Notification", description = "Controlador do recurso Notificação.")
@Slf4j
@RestController
@RequestMapping(path = {"/api"})
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationPagePort notificationPagePort;

    @Operation(summary = "Paginar todos", description = "Recurso para buscar todas as notificações paginadas.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK - requisição bem sucedida e com retorno.",
                            content = {@Content(mediaType = "application/json", array = @ArraySchema(minItems = 0,
                                    schema = @Schema(implementation = NotificationResponse.class)))
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
    @GetMapping(value = "/{version}/notifications", version = "1.0")
    @Retryable(
            maxRetries = 2,
            jitter = 10,
            delay = 1000,
            multiplier = 2
    )
    @PageableParameter
    public ResponseEntity<Page<NotificationResponse>> pageAll(
            @Parameter(hidden = true)
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC, page = 0, size = 5) Pageable paginacao) {

        log.info("\n\n 1.0 \n\n");
        var responsePage = notificationPagePort.pageAll(paginacao);

        return ResponseEntity
                .ok()
                .body(responsePage);
    }

    @GetMapping(value = "/{version}/notifications", version = "2.0")
    @Retryable(
            maxRetries = 2,
            jitter = 10,
            delay = 1000,
            multiplier = 2
    )
    @PageableParameter
    public ResponseEntity<Page<NotificationResponse>> pageAllV2(
            @Parameter(hidden = true)
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC, page = 0, size = 5) Pageable paginacao) {

        log.info("\n\n 2.0 \n\n");
        var responsePage = notificationPagePort.pageAll(paginacao);

        return ResponseEntity
                .ok()
                .body(responsePage);
    }
}

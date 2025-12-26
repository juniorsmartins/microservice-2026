package backend.communication.infraestrutura.controllers;

import backend.communication.aplicacao.dtos.response.NotificationResponse;
import backend.communication.infraestrutura.gateways.NotificationPagePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@Slf4j
@RestController
@RequestMapping(path = {NotificationController.URI_NOTIFICATIONS})
@RequiredArgsConstructor
public class NotificationController {

    protected static final String URI_NOTIFICATIONS = "/v1/notifications";

    private final NotificationPagePort notificationPagePort;

    @GetMapping
    @Retryable(
            maxRetries = 2,
            jitter = 10,
            delay = 1000,
            multiplier = 2
    )
    public ResponseEntity<Page<NotificationResponse>> pageAll(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC, page = 0, size = 5) Pageable paginacao) {

        Random random = new Random();
        if (random.nextDouble() < 0.5) {
            log.info("\n\n Find - Simulando falha temporária ao buscar clientes... \n");
            throw new RuntimeException("Erro temporário ao buscar clientes. Tentando novamente...");
        }

        var responsePage = notificationPagePort.pageAll(paginacao);

        return ResponseEntity
                .ok()
                .body(responsePage);
    }
}

package backend.communication.aplicacao.dtos.response;

import java.time.OffsetDateTime;
import java.util.UUID;

public record NotificationResponse(

        UUID id,

        UUID customerCode,

        String customerEmail,

        String message,

        String reason,

        OffsetDateTime createdAt
) {
}

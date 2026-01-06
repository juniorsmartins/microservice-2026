package backend.communication.infraestrutura.dtos.responses;

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

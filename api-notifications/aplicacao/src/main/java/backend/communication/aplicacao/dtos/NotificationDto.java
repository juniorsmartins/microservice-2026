package backend.communication.aplicacao.dtos;

import java.time.OffsetDateTime;
import java.util.UUID;

public record NotificationDto(

        UUID id,

        UUID customerCode,

        String customerEmail,

        String message,

        String reason,

        OffsetDateTime createdAt
) { }

package backend.core.api_news.dtos;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

public record NewsDto(

        UUID id,
        String hat,
        String title,
        String thinLine,
        String text,
        String author,
        String font,

        // Spring Data Jpa Auditing
        String createdBy,
        String lastModifiedBy,
        OffsetDateTime createdDate,
        OffsetDateTime lastModifiedDate

) implements Serializable {
}

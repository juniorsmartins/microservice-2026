package backend.finance.application.dtos.response;

import java.time.OffsetDateTime;
import java.util.UUID;

public record UserAllResponse(

        UUID id,

        String username,

        boolean active,

        String createdBy,

        String lastModifiedBy,

        OffsetDateTime createdDate,

        OffsetDateTime lastModifiedDate

) {
}

package backend.finance.application.dtos.response;

import java.time.OffsetDateTime;
import java.util.UUID;

public record CustomerAllResponse(

        UUID id,

        String name,

        String email,

        UserAllResponse user,

        boolean active,

        String createdBy,

        String lastModifiedBy,

        OffsetDateTime createdDate,

        OffsetDateTime lastModifiedDate

) {
}

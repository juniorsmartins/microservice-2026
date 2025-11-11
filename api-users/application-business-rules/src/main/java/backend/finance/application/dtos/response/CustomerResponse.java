package backend.finance.application.dtos.response;

import java.util.UUID;

public record CustomerResponse(

        UUID id,

        String name,

        String email,

        UserResponse user,

        boolean active
) {
}

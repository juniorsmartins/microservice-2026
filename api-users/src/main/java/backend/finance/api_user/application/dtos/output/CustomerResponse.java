package backend.finance.api_user.application.dtos.output;

import java.util.UUID;

public record CustomerResponse(

        UUID id,

        String name,

        String email,

        UserResponse user
) {
}

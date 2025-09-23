package backend.finance.api_user.application.dtos.output;

import java.util.UUID;

public record UserResponse(

        UUID id,

        String username
) {
}

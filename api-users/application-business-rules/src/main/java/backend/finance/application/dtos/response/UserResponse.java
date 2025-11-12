package backend.finance.application.dtos.response;

import java.util.UUID;

public record UserResponse(

        UUID id,

        String username,

        boolean active
) {
}

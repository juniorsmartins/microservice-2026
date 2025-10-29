package backend.finance.api_user.application.dtos.internal;

import java.util.UUID;

public record UserDto(

        UUID id,

        String username,

        boolean active
) {
}

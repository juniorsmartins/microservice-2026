package backend.finance.api_user.application.dtos.internal;

import java.util.UUID;

public record CustomerDto(

        UUID id,

        String name,

        String email,

        UserDto user
) {
}

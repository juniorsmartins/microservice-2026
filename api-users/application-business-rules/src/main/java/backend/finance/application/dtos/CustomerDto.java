package backend.finance.application.dtos;

import java.util.UUID;

public record CustomerDto(

        UUID id,

        String name,

        String email,

        UserDto user,

        boolean active
) {
}

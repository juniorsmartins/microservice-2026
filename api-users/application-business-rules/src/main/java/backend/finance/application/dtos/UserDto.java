package backend.finance.application.dtos;

import java.util.UUID;

public record UserDto(

        UUID id,

        String username,

        String password,

        RoleDto role,

        boolean active
) {
}

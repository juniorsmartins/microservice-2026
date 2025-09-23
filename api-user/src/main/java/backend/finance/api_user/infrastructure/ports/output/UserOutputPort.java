package backend.finance.api_user.infrastructure.ports.output;

import backend.finance.api_user.application.dtos.internal.UserDto;

import java.util.Optional;

public interface UserOutputPort {

    Optional<UserDto> findByUsername(String username);
}

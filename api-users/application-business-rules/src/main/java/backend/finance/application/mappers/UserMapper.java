package backend.finance.application.mappers;

import backend.finance.application.dtos.UserDto;
import backend.finance.application.dtos.response.UserResponse;
import backend.finance.enterprise.entities.Usuario;

public interface UserMapper {

    UserDto toDto(Usuario usuario);

    Usuario toEntity(UserDto dto);

    UserResponse toResponse(UserDto dto);
}

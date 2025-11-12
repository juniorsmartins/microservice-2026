package backend.finance.application.mappers;

import backend.finance.application.dtos.UserDto;
import backend.finance.application.dtos.response.UserResponse;
import backend.finance.enterprise.entities.Usuario;

public final class UserMapperImpl implements UserMapper {

    private final RoleMapper roleMapper;

    public UserMapperImpl(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    @Override
    public UserDto toDto(Usuario usuario) {
        var permissao = roleMapper.toDto(usuario.getRole());
        return new UserDto(usuario.getId(), usuario.getUsername(), usuario.getPassword(), permissao, usuario.isActive());
    }

    @Override
    public Usuario toEntity(UserDto dto) {
        var permissao = roleMapper.toEntity(dto.role());
        return new Usuario(dto.id(), dto.username(), dto.password(), permissao, dto.active());
    }

    @Override
    public UserResponse toResponse(UserDto dto) {
        return new UserResponse(dto.id(), dto.username(), dto.active());
    }
}

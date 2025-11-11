package backend.finance.application.mappers;

import backend.finance.application.dtos.RoleDto;
import backend.finance.enterprise.entities.Permissao;
import backend.finance.enterprise.enums.RoleEnum;

public final class RoleMapperImpl implements RoleMapper {

    @Override
    public RoleDto toDto(Permissao permissao) {
        return new RoleDto(permissao.getId(), permissao.getName().name());
    }

    @Override
    public Permissao toEntity(RoleDto dto) {
        return Permissao.create(dto.id(), RoleEnum.valueOf(dto.name()));
    }
}

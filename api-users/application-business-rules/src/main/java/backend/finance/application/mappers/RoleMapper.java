package backend.finance.application.mappers;

import backend.finance.application.dtos.RoleDto;
import backend.finance.enterprise.entities.Permissao;

public interface RoleMapper {

    RoleDto toDto(Permissao permissao);

    Permissao toEntity(RoleDto dto);
}

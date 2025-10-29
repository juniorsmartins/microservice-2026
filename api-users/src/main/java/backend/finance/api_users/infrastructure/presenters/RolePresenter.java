package backend.finance.api_users.infrastructure.presenters;

import backend.finance.api_users.application.dtos.internal.RoleDto;
import backend.finance.api_users.domain.entities.Permissao;
import backend.finance.api_users.infrastructure.jpas.RoleJpa;

public interface RolePresenter {

    RoleDto toRoleDto(RoleJpa roleJpa);

    RoleJpa toRoleJpa(RoleDto roleDto);

    RoleJpa toRoleJpa(Permissao permissao);

    Permissao toEntity(RoleJpa jpa);
}

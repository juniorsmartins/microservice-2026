package backend.finance.api_user.infrastructure.presenters;

import backend.finance.api_user.application.dtos.internal.RoleDto;
import backend.finance.api_user.domain.entities.Permissao;
import backend.finance.api_user.infrastructure.jpas.RoleJpa;

public interface RolePresenter {

    RoleDto toRoleDto(RoleJpa roleJpa);

    RoleJpa toRoleJpa(RoleDto roleDto);

    RoleJpa toRoleJpa(Permissao permissao);
}

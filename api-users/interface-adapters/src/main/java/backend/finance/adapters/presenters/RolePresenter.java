package backend.finance.adapters.presenters;

import backend.finance.adapters.jpas.RoleJpa;
import backend.finance.application.dtos.RoleDto;

public interface RolePresenter {

    RoleDto toRoleDto(RoleJpa roleJpa);

    RoleJpa toRoleJpa(RoleDto roleDto);
}

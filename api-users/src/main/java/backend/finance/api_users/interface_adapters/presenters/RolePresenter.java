package backend.finance.api_users.interface_adapters.presenters;

import backend.finance.api_users.application_business_rules.dtos.internal.RoleDto;
import backend.finance.api_users.enterprise_business_rules.entities.Permissao;
import backend.finance.api_users.frameworks_drivers.jpas.RoleJpa;

public interface RolePresenter {

    RoleDto toRoleDto(RoleJpa roleJpa);

    RoleJpa toRoleJpa(RoleDto roleDto);

    RoleJpa toRoleJpa(Permissao permissao);

    Permissao toEntity(RoleJpa jpa);
}

package backend.finance.api_users.interface_adapters.presenters;

import backend.finance.api_users.application_business_rules.dtos.internal.RoleDto;
import backend.finance.api_users.enterprise_business_rules.entities.Permissao;
import backend.finance.api_users.interface_adapters.jpas.RoleJpa;
import org.springframework.stereotype.Component;

@Component
public class RolePresenterImpl implements RolePresenter {

    @Override
    public RoleDto toRoleDto(RoleJpa roleJpa) {
        return new RoleDto(roleJpa.getId(), roleJpa.getName());
    }

    @Override
    public RoleJpa toRoleJpa(RoleDto roleDto) {
        return new RoleJpa(roleDto.id(), roleDto.name());
    }

    @Override
    public RoleJpa toRoleJpa(Permissao permissao) {
        return new RoleJpa(permissao.getId(), permissao.getName());
    }

    @Override
    public Permissao toEntity(RoleJpa jpa) {
        return Permissao.create(jpa.getId(), jpa.getName());
    }
}

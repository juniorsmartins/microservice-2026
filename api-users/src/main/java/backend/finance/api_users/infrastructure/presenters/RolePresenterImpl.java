package backend.finance.api_users.infrastructure.presenters;

import backend.finance.api_users.application.dtos.internal.RoleDto;
import backend.finance.api_users.domain.entities.Permissao;
import backend.finance.api_users.infrastructure.jpas.RoleJpa;
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

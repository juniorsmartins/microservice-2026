package backend.finance.adapters.presenters;

import backend.finance.adapters.jpas.RoleJpa;
import backend.finance.application.dtos.RoleDto;
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
}

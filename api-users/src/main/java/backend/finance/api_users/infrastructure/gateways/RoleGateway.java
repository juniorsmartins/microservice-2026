package backend.finance.api_users.infrastructure.gateways;

import backend.finance.api_users.application.dtos.internal.RoleDto;
import backend.finance.api_users.domain.enums.RoleEnum;
import backend.finance.api_users.infrastructure.ports.output.RoleOutputPort;
import backend.finance.api_users.infrastructure.presenters.RolePresenter;
import backend.finance.api_users.infrastructure.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RoleGateway implements RoleOutputPort {

    private final RoleRepository roleRepository;

    private final RolePresenter rolePresenter;

    @Override
    public Optional<RoleDto> findByName(RoleEnum name) {
        return roleRepository.findByName(name)
                .map(rolePresenter::toRoleDto);
    }

    @Override
    public RoleDto save(RoleDto roleDto) {
        var roleJpa = rolePresenter.toRoleJpa(roleDto);
        var roleSave = roleRepository.save(roleJpa);
        return rolePresenter.toRoleDto(roleSave);
    }
}

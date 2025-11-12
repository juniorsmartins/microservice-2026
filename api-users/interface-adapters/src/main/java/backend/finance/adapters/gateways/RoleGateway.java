package backend.finance.adapters.gateways;

import backend.finance.adapters.presenters.RolePresenter;
import backend.finance.adapters.repositories.RoleRepository;
import backend.finance.application.dtos.RoleDto;
import backend.finance.application.ports.output.RoleOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RoleGateway implements RoleOutputPort {

    private final RoleRepository roleRepository;

    private final RolePresenter rolePresenter;

    @Override
    public Optional<RoleDto> findByName(String name) {
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

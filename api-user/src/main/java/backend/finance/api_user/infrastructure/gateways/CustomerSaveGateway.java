package backend.finance.api_user.infrastructure.gateways;

import backend.finance.api_user.application.configs.exception.http404.RoleNotFoundCustomException;
import backend.finance.api_user.application.dtos.input.CustomerRequest;
import backend.finance.api_user.application.dtos.internal.CustomerDto;
import backend.finance.api_user.domain.enums.RoleEnum;
import backend.finance.api_user.infrastructure.jpas.RoleJpa;
import backend.finance.api_user.infrastructure.ports.output.CustomerSaveOutputPort;
import backend.finance.api_user.infrastructure.presenters.CustomerPresenter;
import backend.finance.api_user.infrastructure.repositories.CustomerRepository;
import backend.finance.api_user.infrastructure.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class CustomerSaveGateway implements CustomerSaveOutputPort {

    private final CustomerRepository customerRepository;

    private final RoleRepository roleRepository;

    private final CustomerPresenter customerPresenter;

    @Transactional
    @Override
    public CustomerDto save(CustomerRequest customerRequest) {
        var roleJpa = getOrCreateRole(customerRequest.user().role());
        var customerJpa = customerPresenter.toCustomerJpa(customerRequest, roleJpa);
        var customerSave = customerRepository.save(customerJpa);
        return customerPresenter.toCustomerDto(customerSave);
    }

    private RoleJpa getOrCreateRole(String name) {
        try {
            var roleEnum = RoleEnum.valueOf(name);
            var roleJpa = roleRepository.findByName(roleEnum);
            return roleJpa.orElseGet(() -> roleRepository.save(new RoleJpa(null, roleEnum)));
        } catch (IllegalArgumentException e) {
            throw new RoleNotFoundCustomException(name);
        }
    }
}

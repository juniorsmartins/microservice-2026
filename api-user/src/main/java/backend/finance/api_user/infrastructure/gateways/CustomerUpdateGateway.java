package backend.finance.api_user.infrastructure.gateways;

import backend.finance.api_user.application.configs.exception.http404.CustomerNotFoundCustomException;
import backend.finance.api_user.application.configs.exception.http404.RoleNotFoundCustomException;
import backend.finance.api_user.application.dtos.input.CustomerRequest;
import backend.finance.api_user.application.dtos.internal.CustomerDto;
import backend.finance.api_user.domain.enums.RoleEnum;
import backend.finance.api_user.infrastructure.jpas.RoleJpa;
import backend.finance.api_user.infrastructure.ports.output.CustomerUpdateOutputPort;
import backend.finance.api_user.infrastructure.presenters.CustomerPresenter;
import backend.finance.api_user.infrastructure.repositories.CustomerRepository;
import backend.finance.api_user.infrastructure.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CustomerUpdateGateway implements CustomerUpdateOutputPort {

    private final CustomerRepository customerRepository;

    private final RoleRepository roleRepository;

    private final CustomerPresenter customerPresenter;

    @Override
    public CustomerDto update(UUID customerId, CustomerRequest customerRequest) {
        var roleJpa = getOrCreateRole(customerRequest.user().role());

        return customerRepository.findById(customerId)
                .map(customerJpa -> {
                    BeanUtils.copyProperties(customerRequest, customerJpa, "id");
                    BeanUtils.copyProperties(customerRequest.user(), customerJpa.getUser(), "id", "password");
                    customerJpa.getUser().setRole(roleJpa);
                    return customerJpa;
                })
                .map(customerPresenter::toCustomerDto)
                .orElseThrow(() -> new CustomerNotFoundCustomException(customerId));
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

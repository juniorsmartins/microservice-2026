package backend.finance.api_user.infrastructure.gateways;

import backend.finance.api_user.application.configs.exception.http404.CustomerNotFoundCustomException;
import backend.finance.api_user.application.configs.exception.http404.RoleNotFoundCustomException;
import backend.finance.api_user.application.dtos.internal.CustomerDto;
import backend.finance.api_user.domain.entities.Customer;
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
    public CustomerDto update(UUID customerId, Customer customer) {
        var roleJpa = getOrCreateRole(customer.getUser().getRole().getName().getValue());

        return customerRepository.findById(customerId)
                .map(customerJpa -> {
                    BeanUtils.copyProperties(customer, customerJpa, "id");
                    BeanUtils.copyProperties(customer.getUser(), customerJpa.getUser(), "id", "password");
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

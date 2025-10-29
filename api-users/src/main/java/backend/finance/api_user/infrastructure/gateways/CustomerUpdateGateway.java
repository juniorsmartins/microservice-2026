package backend.finance.api_user.infrastructure.gateways;

import backend.finance.api_user.application.configs.exception.http404.CustomerNotFoundCustomException;
import backend.finance.api_user.application.configs.exception.http404.RoleNotFoundCustomException;
import backend.finance.api_user.application.dtos.internal.CustomerDto;
import backend.finance.api_user.domain.entities.Customer;
import backend.finance.api_user.infrastructure.ports.output.CustomerUpdateOutputPort;
import backend.finance.api_user.infrastructure.presenters.CustomerPresenter;
import backend.finance.api_user.infrastructure.repositories.CustomerRepository;
import backend.finance.api_user.infrastructure.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class CustomerUpdateGateway implements CustomerUpdateOutputPort {

    private final CustomerRepository customerRepository;

    private final RoleRepository roleRepository;

    private final CustomerPresenter customerPresenter;

    @Transactional
    @Override
    public CustomerDto update(Customer customer) {
        var roleEnum = customer.getUser().getRole().getName();
        var roleJpa = roleRepository.findByName(roleEnum)
                .orElseThrow(() -> new RoleNotFoundCustomException(roleEnum.name()));

        return customerRepository.findById(customer.getId())
                .map(customerJpa -> {
                    customerJpa.setName(customer.getName());
                    customerJpa.setEmail(customer.getEmail());
                    customerJpa.getUser().setUsername(customer.getUser().getUsername());
                    customerJpa.getUser().setPassword(customer.getUser().getPassword());
                    customerJpa.getUser().setRole(roleJpa);
                    return customerJpa;
                })
                .map(customerRepository::save)
                .map(customerPresenter::toDto)
                .orElseThrow(() -> new CustomerNotFoundCustomException(customer.getId()));
    }
}

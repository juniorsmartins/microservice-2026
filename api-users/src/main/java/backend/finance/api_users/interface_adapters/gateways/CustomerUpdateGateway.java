package backend.finance.api_users.interface_adapters.gateways;

import backend.finance.api_users.enterprise_business_rules.exception.http404.CustomerNotFoundCustomException;
import backend.finance.api_users.enterprise_business_rules.exception.http404.RoleNotFoundCustomException;
import backend.finance.api_users.enterprise_business_rules.entities.Customer;
import backend.finance.api_users.application_business_rules.ports.output.CustomerUpdateOutputPort;
import backend.finance.api_users.interface_adapters.presenters.CustomerPresenter;
import backend.finance.api_users.frameworks_drivers.repositories.CustomerRepository;
import backend.finance.api_users.frameworks_drivers.repositories.RoleRepository;
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
    public Customer update(Customer customer) {
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
                .map(customerPresenter::toEntity)
                .orElseThrow(() -> new CustomerNotFoundCustomException(customer.getId()));
    }
}

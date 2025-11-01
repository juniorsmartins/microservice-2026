package backend.finance.api_users.interface_adapters.gateways;

import backend.finance.api_users.enterprise_business_rules.entities.Customer;
import backend.finance.api_users.application_business_rules.ports.output.CustomerSaveOutputPort;
import backend.finance.api_users.interface_adapters.presenters.CustomerPresenter;
import backend.finance.api_users.interface_adapters.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class CustomerSaveGateway implements CustomerSaveOutputPort {

    private final CustomerRepository customerRepository;

    private final CustomerPresenter customerPresenter;

    @Transactional
    @Override
    public Customer save(Customer customer) {
        var customerJpa = customerPresenter.toJpa(customer);
        var customerSave = customerRepository.save(customerJpa);
        return customerPresenter.toEntity(customerSave);
    }
}

package backend.finance.api_users.infrastructure.gateways;

import backend.finance.api_users.domain.entities.Customer;
import backend.finance.api_users.infrastructure.ports.output.CustomerSaveOutputPort;
import backend.finance.api_users.infrastructure.presenters.CustomerPresenter;
import backend.finance.api_users.infrastructure.repositories.CustomerRepository;
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

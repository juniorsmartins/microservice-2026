package backend.finance.api_user.infrastructure.gateways;

import backend.finance.api_user.application.dtos.internal.CustomerDto;
import backend.finance.api_user.domain.entities.Customer;
import backend.finance.api_user.infrastructure.ports.output.CustomerSaveOutputPort;
import backend.finance.api_user.infrastructure.presenters.CustomerPresenter;
import backend.finance.api_user.infrastructure.repositories.CustomerRepository;
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
    public CustomerDto save(Customer customer) {
        var customerJpa = customerPresenter.toJpa(customer);
        var customerSave = customerRepository.save(customerJpa);
        return customerPresenter.toDto(customerSave);
    }
}

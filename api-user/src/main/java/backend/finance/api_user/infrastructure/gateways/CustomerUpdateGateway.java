package backend.finance.api_user.infrastructure.gateways;

import backend.finance.api_user.application.configs.exception.http404.CustomerNotFoundCustomException;
import backend.finance.api_user.application.dtos.internal.CustomerDto;
import backend.finance.api_user.domain.entities.Customer;
import backend.finance.api_user.infrastructure.ports.output.CustomerUpdateOutputPort;
import backend.finance.api_user.infrastructure.presenters.CustomerPresenter;
import backend.finance.api_user.infrastructure.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class CustomerUpdateGateway implements CustomerUpdateOutputPort {

    private final CustomerRepository customerRepository;

    private final CustomerPresenter customerPresenter;

    @Transactional
    @Override
    public CustomerDto update(Customer customer) {

        return customerRepository.findById(customer.getId())
                .map(customerJpa -> {
                    customerJpa.setName(customer.getName());
                    customerJpa.setEmail(customer.getEmail());
                    customerJpa.getUser().setUsername(customer.getUser().getUsername());
                    customerJpa.getUser().getRole().setId(customer.getUser().getRole().getId());
                    customerJpa.getUser().getRole().setName(customer.getUser().getRole().getName());
                    return customerJpa;
                })
                .map(customerPresenter::toCustomerDto)
                .orElseThrow(() -> new CustomerNotFoundCustomException(customer.getId()));
    }
}

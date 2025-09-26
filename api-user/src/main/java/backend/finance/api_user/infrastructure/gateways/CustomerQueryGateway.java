package backend.finance.api_user.infrastructure.gateways;

import backend.finance.api_user.application.dtos.internal.CustomerDto;
import backend.finance.api_user.infrastructure.ports.output.CustomerQueryOutputPort;
import backend.finance.api_user.infrastructure.presenters.CustomerPresenter;
import backend.finance.api_user.infrastructure.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CustomerQueryGateway implements CustomerQueryOutputPort {

    private final CustomerRepository customerRepository;

    private final CustomerPresenter customerPresenter;

    @Transactional(readOnly = true)
    @Override
    public Optional<CustomerDto> findById(UUID id) {
        return customerRepository.findById(id)
                .map(customerPresenter::toCustomerDto);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<CustomerDto> findByEmail(String email) {
        return customerRepository.findByEmail(email)
                .map(customerPresenter::toCustomerDto);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<CustomerDto> findByUsername(String username) {
        return customerRepository.findByUserUsername(username)
                .map(customerPresenter::toCustomerDto);
    }
}

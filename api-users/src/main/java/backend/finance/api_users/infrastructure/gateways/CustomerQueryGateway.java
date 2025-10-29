package backend.finance.api_users.infrastructure.gateways;

import backend.finance.api_users.domain.entities.Customer;
import backend.finance.api_users.infrastructure.ports.output.CustomerQueryOutputPort;
import backend.finance.api_users.infrastructure.presenters.CustomerPresenter;
import backend.finance.api_users.infrastructure.repositories.CustomerRepository;
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
    public Optional<Customer> findByIdAndActiveTrue(UUID id) {
        return customerRepository.findByIdAndActiveTrue(id)
                .map(customerPresenter::toEntity);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Customer> findByEmail(String email) {
        return customerRepository.findByEmail(email)
                .map(customerPresenter::toEntity);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Customer> findByUsername(String username) {
        return customerRepository.findByUserUsername(username)
                .map(customerPresenter::toEntity);
    }
}

package backend.finance.api_users.interface_adapters.gateways;

import backend.finance.api_users.enterprise_business_rules.entities.Customer;
import backend.finance.api_users.application_business_rules.ports.output.CustomerQueryOutputPort;
import backend.finance.api_users.interface_adapters.presenters.CustomerPresenter;
import backend.finance.api_users.frameworks_drivers.repositories.CustomerRepository;
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

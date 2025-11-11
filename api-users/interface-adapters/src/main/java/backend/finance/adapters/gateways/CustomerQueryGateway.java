package backend.finance.adapters.gateways;

import backend.finance.adapters.presenters.CustomerPresenter;
import backend.finance.adapters.repositories.CustomerRepository;
import backend.finance.application.dtos.CustomerDto;
import backend.finance.application.ports.output.CustomerQueryOutputPort;
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
    public Optional<CustomerDto> findByIdAndActiveTrue(UUID id) {
        return customerRepository.findByIdAndActiveTrue(id)
                .map(customerPresenter::toDto);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<CustomerDto> findByEmail(String email) {
        return customerRepository.findByEmail(email)
                .map(customerPresenter::toDto);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<CustomerDto> findByUsername(String username) {
        return customerRepository.findByUserUsername(username)
                .map(customerPresenter::toDto);
    }
}

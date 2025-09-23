package backend.finance.api_user.infrastructure.gateways;

import backend.finance.api_user.application.dtos.input.CustomerRequest;
import backend.finance.api_user.application.dtos.internal.CustomerDto;
import backend.finance.api_user.infrastructure.ports.output.CustomerOutputPort;
import backend.finance.api_user.infrastructure.repositories.CustomerRepository;
import backend.finance.api_user.infrastructure.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CustomerGateway implements CustomerOutputPort {

    private final CustomerRepository customerRepository;

    private final RoleRepository roleRepository;

    @Override
    public CustomerDto save(CustomerRequest customerRequest) {
        return null;
    }

    @Override
    public Optional<CustomerDto> findByEmail(String email) {
        return Optional.empty();
    }
}

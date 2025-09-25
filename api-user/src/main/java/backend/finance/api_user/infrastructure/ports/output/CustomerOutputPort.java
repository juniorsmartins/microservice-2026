package backend.finance.api_user.infrastructure.ports.output;

import backend.finance.api_user.application.dtos.input.CustomerRequest;
import backend.finance.api_user.application.dtos.internal.CustomerDto;

import java.util.Optional;
import java.util.UUID;

public interface CustomerOutputPort {

    CustomerDto save(CustomerRequest customerRequest);

    CustomerDto update(UUID customerId, CustomerRequest customerRequest);

    void deleteById(UUID id);

    Optional<CustomerDto> findById(UUID id);

    Optional<CustomerDto> findByEmail(String email);

    Optional<CustomerDto> findByUsername(String username);
}

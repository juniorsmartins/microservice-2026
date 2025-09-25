package backend.finance.api_user.infrastructure.ports.output;

import backend.finance.api_user.application.dtos.input.CustomerRequest;
import backend.finance.api_user.application.dtos.internal.CustomerDto;

import java.util.Optional;
import java.util.UUID;

public interface CustomerOutputPort {

    CustomerDto save(CustomerRequest customerRequest);

    Optional<CustomerDto> findByEmail(String email);

    Optional<CustomerDto> findById(UUID id);

    void deleteById(UUID id);
}

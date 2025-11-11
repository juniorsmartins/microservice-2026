package backend.finance.application.ports.output;

import backend.finance.application.dtos.CustomerDto;

import java.util.Optional;
import java.util.UUID;

public interface CustomerQueryOutputPort {

    Optional<CustomerDto> findActiveById(UUID id);

    Optional<CustomerDto> findByEmail(String email);

    Optional<CustomerDto> findByUsername(String username);
}

package backend.finance.api_users.infrastructure.ports.output;

import backend.finance.api_users.domain.entities.Customer;

import java.util.Optional;
import java.util.UUID;

public interface CustomerQueryOutputPort {

    Optional<Customer> findByIdAndActiveTrue(UUID id);

    Optional<Customer> findByEmail(String email);

    Optional<Customer> findByUsername(String username);
}

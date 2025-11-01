package backend.finance.api_users.application_business_rules.ports.output;

import backend.finance.api_users.enterprise_business_rules.entities.Customer;

import java.util.Optional;
import java.util.UUID;

public interface CustomerQueryOutputPort {

    Optional<Customer> findByIdAndActiveTrue(UUID id);

    Optional<Customer> findByEmail(String email);

    Optional<Customer> findByUsername(String username);
}

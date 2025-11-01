package backend.finance.api_users.interface_adapters.repositories;

import backend.finance.api_users.interface_adapters.jpas.CustomerJpa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<CustomerJpa, UUID> {

    Optional<CustomerJpa> findByIdAndActiveTrue(UUID id);

    Optional<CustomerJpa> findByEmail(String email);

    Optional<CustomerJpa> findByUserUsername(String username);
}

package backend.finance.adapters.repositories;

import backend.finance.adapters.jpas.CustomerJpa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<CustomerJpa, UUID> {

    Optional<CustomerJpa> findByIdAndActiveTrue(UUID id);

    Optional<CustomerJpa> findByEmail(String email);

    Optional<CustomerJpa> findByUserUsername(String username);
}

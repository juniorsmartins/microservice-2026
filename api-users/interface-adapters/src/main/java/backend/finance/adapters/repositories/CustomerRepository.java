package backend.finance.adapters.repositories;

import backend.finance.adapters.jpas.CustomerJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<CustomerJpa, UUID> {

    @Query("SELECT c FROM CustomerJpa c WHERE c.id = :id AND c.active = true")
    Optional<CustomerJpa> findActiveById(@Param("id") UUID id);

    Optional<CustomerJpa> findByEmail(String email);

    Optional<CustomerJpa> findByUserUsername(String username);
}

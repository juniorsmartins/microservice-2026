package backend.finance.adapters.repositories;

import backend.finance.adapters.jpas.RoleJpa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<RoleJpa, UUID> {

    Optional<RoleJpa> findByName(String name);
}

package backend.finance.api_users.infrastructure.repositories;

import backend.finance.api_users.domain.enums.RoleEnum;
import backend.finance.api_users.infrastructure.jpas.RoleJpa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<RoleJpa, UUID> {

    Optional<RoleJpa> findByName(RoleEnum name);
}

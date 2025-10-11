package backend.finance.api_user.infrastructure.repositories;

import backend.finance.api_user.domain.enums.RoleEnum;
import backend.finance.api_user.infrastructure.jpas.RoleJpa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<RoleJpa, UUID> {

    Optional<RoleJpa> findByName(RoleEnum name);
}

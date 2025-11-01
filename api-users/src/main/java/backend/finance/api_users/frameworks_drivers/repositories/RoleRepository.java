package backend.finance.api_users.frameworks_drivers.repositories;

import backend.finance.api_users.enterprise_business_rules.enums.RoleEnum;
import backend.finance.api_users.frameworks_drivers.jpas.RoleJpa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<RoleJpa, UUID> {

    Optional<RoleJpa> findByName(RoleEnum name);
}

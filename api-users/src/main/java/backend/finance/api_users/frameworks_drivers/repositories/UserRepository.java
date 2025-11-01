package backend.finance.api_users.frameworks_drivers.repositories;

import backend.finance.api_users.frameworks_drivers.jpas.UserJpa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserJpa, UUID> {

    Optional<UserJpa> findByUsername(String username);
}

package backend.finance.api_user.infrastructure.repositories;

import backend.finance.api_user.infrastructure.jpas.UserJpa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserJpa, UUID> {

    Optional<UserJpa> findByUsername(String username);
}

package backend.finance.adapters.repositories;

import backend.finance.adapters.jpas.UserJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserJpa, UUID>, RevisionRepository<UserJpa, UUID, Integer>  {
}

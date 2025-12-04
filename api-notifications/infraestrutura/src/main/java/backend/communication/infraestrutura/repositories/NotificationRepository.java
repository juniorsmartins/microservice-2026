package backend.communication.infraestrutura.repositories;

import backend.communication.infraestrutura.jpas.NotificationJpa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<NotificationJpa, UUID> {

    Optional<NotificationJpa> findByCustomerCodeAndReason(UUID id, String reason);
}

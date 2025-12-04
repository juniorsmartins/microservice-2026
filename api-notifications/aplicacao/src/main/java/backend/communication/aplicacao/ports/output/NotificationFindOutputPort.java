package backend.communication.aplicacao.ports.output;

import backend.communication.aplicacao.dtos.NotificationDto;

import java.util.Optional;
import java.util.UUID;

public interface NotificationFindOutputPort {

    Optional<NotificationDto> findByCustomerCodeAndReason(UUID id, String reason);
}

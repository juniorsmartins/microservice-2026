package backend.communication.infraestrutura.gateways;

import backend.communication.aplicacao.dtos.NotificationDto;
import backend.communication.aplicacao.ports.output.NotificationFindOutputPort;
import backend.communication.infraestrutura.presenters.NotificationPresenter;
import backend.communication.infraestrutura.repositories.NotificationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class NotificationFindGateway implements NotificationFindOutputPort {

    private final NotificationRepository notificationRepository;

    private final NotificationPresenter notificationPresenter;

    @Transactional
    @Override
    public Optional<NotificationDto> findByCustomerCodeAndReason(UUID id, String reason) {
        return notificationRepository.findByCustomerCodeAndReason(id, reason)
                .map(notificationPresenter::toDto);
    }
}

package backend.communication.infraestrutura.gateways;

import backend.communication.aplicacao.dtos.NotificationDto;
import backend.communication.aplicacao.ports.output.NotificationSaveOutputPort;
import backend.communication.infraestrutura.presenters.NotificationPresenter;
import backend.communication.infraestrutura.repositories.NotificationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NotificationSaveGateway implements NotificationSaveOutputPort {

    private final NotificationRepository notificationRepository;

    private final NotificationPresenter notificationPresenter;

    @Transactional
    @Override
    public NotificationDto save(NotificationDto dto) {
        var notificationJpa = notificationPresenter.toJpa(dto);
        var notificationJpaSaved = notificationRepository.save(notificationJpa);
        return notificationPresenter.toDto(notificationJpaSaved);
    }
}

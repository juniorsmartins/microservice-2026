package backend.communication.infraestrutura.gateways;

import backend.communication.aplicacao.dtos.NotificationDto;
import backend.communication.aplicacao.dtos.response.NotificationResponse;
import backend.communication.aplicacao.ports.output.NotificationFindOutputPort;
import backend.communication.infraestrutura.presenters.NotificationPresenter;
import backend.communication.infraestrutura.repositories.NotificationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class NotificationQueryGateway implements NotificationFindOutputPort, NotificationPagePort {

    private final NotificationRepository notificationRepository;

    private final NotificationPresenter notificationPresenter;

    @Transactional
    @Override
    public Optional<NotificationDto> findByCustomerCodeAndReason(UUID id, String reason) {
        return notificationRepository.findByCustomerCodeAndReason(id, reason)
                .map(notificationPresenter::toDto);
    }

    @Transactional
    @Override
    public Page<NotificationResponse> pageAll(Pageable paginacao) {
        return notificationRepository.findAll(paginacao)
                .map(notificationPresenter::toResponse);
    }
}

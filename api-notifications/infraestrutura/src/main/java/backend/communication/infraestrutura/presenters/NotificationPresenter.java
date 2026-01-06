package backend.communication.infraestrutura.presenters;

import backend.communication.aplicacao.dtos.NotificationDto;
import backend.communication.infraestrutura.dtos.responses.NotificationResponse;
import backend.communication.infraestrutura.jpas.NotificationJpa;

public interface NotificationPresenter {

    NotificationJpa toJpa(NotificationDto dto);

    NotificationDto toDto(NotificationJpa jpa);

    NotificationResponse toResponse(NotificationDto dto);

    NotificationResponse toResponse(NotificationJpa jpa);
}

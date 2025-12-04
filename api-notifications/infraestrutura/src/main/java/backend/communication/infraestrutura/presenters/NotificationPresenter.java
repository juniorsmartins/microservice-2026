package backend.communication.infraestrutura.presenters;

import backend.communication.aplicacao.dtos.NotificationDto;
import backend.communication.infraestrutura.jpas.NotificationJpa;

public interface NotificationPresenter {

    NotificationJpa toJpa(NotificationDto dto);

    NotificationDto toDto(NotificationJpa jpa);
}

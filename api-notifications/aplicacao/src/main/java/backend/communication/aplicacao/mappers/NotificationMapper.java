package backend.communication.aplicacao.mappers;

import backend.communication.aplicacao.dtos.NotificationDto;
import backend.communication.dominio.entities.Notification;

public interface NotificationMapper {

    NotificationDto toDto(Notification notification);
}

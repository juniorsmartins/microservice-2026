package backend.communication.aplicacao.mappers;

import backend.communication.aplicacao.dtos.NotificationDto;
import backend.communication.dominio.entities.Notification;

public class NotificationMapperImpl implements NotificationMapper {

    @Override
    public NotificationDto toDto(Notification notification) {
        return new NotificationDto(notification.getId(), notification.getCustomerCode(),
                notification.getCustomerEmail(), notification.getMessage(), notification.getReason().getValue(),
                notification.getCreatedAt());
    }
}

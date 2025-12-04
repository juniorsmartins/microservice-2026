package backend.communication.aplicacao.ports.output;

import backend.communication.aplicacao.dtos.NotificationDto;

public interface NotificationSaveOutputPort {

    NotificationDto save(NotificationDto dto);
}

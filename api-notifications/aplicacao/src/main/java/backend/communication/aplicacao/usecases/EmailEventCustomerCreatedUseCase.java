package backend.communication.aplicacao.usecases;

import backend.communication.aplicacao.mappers.NotificationMapper;
import backend.communication.aplicacao.ports.input.EmailEventCustomerCreatedInputPort;
import backend.communication.aplicacao.ports.output.NotificationSaveOutputPort;
import backend.communication.dominio.entities.Notification;
import backend.communication.dominio.enuns.ReasonEnum;

import java.util.UUID;

public class EmailEventCustomerCreatedUseCase implements EmailEventCustomerCreatedInputPort {

    private final NotificationSaveOutputPort notificationSaveOutputPort;

    private final NotificationMapper notificationMapper;

    public EmailEventCustomerCreatedUseCase(NotificationSaveOutputPort notificationSaveOutputPort, NotificationMapper notificationMapper) {
        this.notificationSaveOutputPort = notificationSaveOutputPort;
        this.notificationMapper = notificationMapper;
    }

    @Override
    public void create(String id, String nome, String email) {

        var message = prepareMessage(nome);
        var notification = checkNotification(id, email, message);
        var notificationDto = notificationMapper.toDto(notification);
        var notificationDtoSaved = notificationSaveOutputPort.save(notificationDto);
    }

    private String prepareMessage(String nome) {
        return String.format("""
               Prezado(a), %s.\s

               Seu cadastro na plataforma foi concluído com sucesso.
              \s
               Atenciosamente, equipe Microsserviços 2026.
              \s""", nome);
    }

    private Notification checkNotification(String id, String email, String message) {
        return new Notification(UUID.fromString(id), email, message, ReasonEnum.CUSTOMER_CREATED);
    }
}

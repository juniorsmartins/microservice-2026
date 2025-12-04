package backend.communication.aplicacao.usecases;

import backend.communication.aplicacao.mappers.NotificationMapper;
import backend.communication.aplicacao.ports.input.EmailEventCustomerCreatedInputPort;
import backend.communication.aplicacao.ports.output.EmailOutputPort;
import backend.communication.aplicacao.ports.output.NotificationFindOutputPort;
import backend.communication.aplicacao.ports.output.NotificationSaveOutputPort;
import backend.communication.dominio.entities.Notification;
import backend.communication.dominio.enuns.ReasonEnum;

import java.util.UUID;
import java.util.logging.Logger;

public class EmailEventCustomerCreatedUseCase implements EmailEventCustomerCreatedInputPort {

    private static final Logger log = Logger.getLogger(EmailEventCustomerCreatedUseCase.class.getName());

    private final NotificationSaveOutputPort notificationSaveOutputPort;

    private final NotificationFindOutputPort notificationFindOutputPort;

    private final NotificationMapper notificationMapper;

    private final EmailOutputPort emailOutputPort;

    public EmailEventCustomerCreatedUseCase(
            NotificationSaveOutputPort notificationSaveOutputPort,
            NotificationFindOutputPort notificationFindOutputPort,
            NotificationMapper notificationMapper,
            EmailOutputPort emailOutputPort) {
        this.notificationSaveOutputPort = notificationSaveOutputPort;
        this.notificationFindOutputPort = notificationFindOutputPort;
        this.notificationMapper = notificationMapper;
        this.emailOutputPort = emailOutputPort;
    }

    @Override
    public void create(String id, String nome, String email) {

        UUID customerCode = parseUUID(id);
        if (customerCode == null) return;

        if (checkIdempotency(customerCode, ReasonEnum.CUSTOMER_CREATED.getValue())) {
            log.info("Evento de customer " + customerCode + " já foi processado (idempotência).");
            return;
        }

        var message = buildMessage(nome);
        var notification = new Notification(customerCode, email, message, ReasonEnum.CUSTOMER_CREATED);
        var notificationDto = notificationMapper.toDto(notification);
        var notificationDtoSaved = notificationSaveOutputPort.save(notificationDto);

        try {
            emailOutputPort.sendEmail(notificationDtoSaved.customerEmail(), "Cadastro", message);
        } catch (Exception e) {
            log.severe("Falha grave no envio de email para: " + email + ". Causa: " + e.getMessage());
        }

    }

    private UUID parseUUID(String id) {
        try {
            return UUID.fromString(id);

        } catch (IllegalArgumentException e) {
            log.severe("Conversão inválida para UUID recebido: " + id);
            return null;
        }
    }

    private boolean checkIdempotency(UUID id, String reason) {
        return notificationFindOutputPort.findByCustomerCodeAndReason(id, reason)
                .isPresent();
    }

    private String buildMessage(String nome) {
        return """
            Prezado(a) %s,

            Seu cadastro na plataforma foi concluído com sucesso.

            Atenciosamente,
            Equipe Microsserviços 2026.
            """.formatted(nome);
    }
}

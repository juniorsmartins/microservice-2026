package backend.communication.infraestrutura.email;

import backend.communication.aplicacao.ports.output.EmailOutputPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailSender implements EmailOutputPort {

    private final EmailPropertiesConfig emailPropertiesConfig;

    private final JavaMailSender javaMailSender;

    @Override
    public void sendEmail(String destinatario, String assunto, String mensagem) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailPropertiesConfig.mailUsername);
        message.setTo(destinatario);
        message.setSubject(assunto);
        message.setText(mensagem);

        try {

            javaMailSender.send(message);
            log.info("\n\n sendEmail - Email enviado com sucesso: {} \n", message);

        } catch (MailException e) {
            log.error("\n\n sendEmail - Erro ao enviar email: {} \n", e.getMessage());
        }
    }
}

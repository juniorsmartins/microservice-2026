package backend.communication.aplicacao.ports.output;

public interface EmailOutputPort {

    void sendEmail(String destinatario, String assunto, String mensagem);
}

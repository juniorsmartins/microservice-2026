package backend.communication.aplicacao.ports.input;

public interface EmailEventCustomerCreatedInputPort {

    void create(String id, String nome, String email);
}

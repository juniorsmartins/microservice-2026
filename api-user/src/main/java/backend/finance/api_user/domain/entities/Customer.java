package backend.finance.api_user.domain.entities;

import backend.finance.api_user.application.dtos.input.CustomerRequest;
import lombok.Getter;

import java.util.UUID;

@Getter
public final class Customer {

    private final UUID id;

    private final String name;

    private final String email;

    private final Usuario user;

    private Customer(UUID id, String name, String email, Usuario user) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.user = user;
    }

    public static Customer create(CustomerRequest request) {
        var usuario = Usuario.create(request.user());
        return new Customer(null, request.name(), request.email(), usuario);
    }
}

package backend.finance.api_user.domain.entities;

import backend.finance.api_user.application.dtos.input.CustomerRequest;
import backend.finance.api_user.application.dtos.internal.RoleDto;
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

    public static Customer create(UUID id, CustomerRequest request, RoleDto roleDto) {
        var usuario = Usuario.create(request.user(), roleDto);
        return new Customer(id, request.name(), request.email(), usuario);
    }
}

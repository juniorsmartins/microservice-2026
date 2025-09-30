package backend.finance.api_user.domain.entities;

import backend.finance.api_user.application.configs.exception.http400.AllNullFieldsCustomException;
import backend.finance.api_user.application.dtos.input.UserRequest;
import lombok.Getter;

import java.util.UUID;

@Getter
public final class Usuario {

    private final UUID id;

    private final String username;

    private final String password;

    private final Permissao role;

    private Usuario(UUID id, String username, String password, Permissao role) {
        this.id = id;
        this.username = checkNotBlank("username", username);
        this.password = checkNotBlank("password", password);
        this.role = role;
    }

    public static Usuario create(UserRequest request, Permissao permissao) {
        return new Usuario(null, request.username(), request.password(), permissao);
    }

    private String checkNotBlank(String fieldName, String value) {
        if (value == null || value.isBlank()) {
            throw new AllNullFieldsCustomException(fieldName);
        }
        return value;
    }
}

package backend.finance.api_user.domain.entities;

import backend.finance.api_user.application.configs.exception.http400.AllNullFieldsCustomException;
import backend.finance.api_user.application.configs.exception.http400.AttributeExceededMaximumLimitException;
import lombok.Getter;

import java.util.UUID;

import static backend.finance.api_user.domain.constant.ConstantsValidation.PASSWORD_SIZE_MAX;
import static backend.finance.api_user.domain.constant.ConstantsValidation.USERNAME_SIZE_MAX;

@Getter
public final class Usuario {

    private final UUID id;

    private final String username;

    private final String password;

    private final Permissao role;

    private final boolean active;

    private Usuario(UUID id, String username, String password, Permissao role, boolean active) {
        this.id = id;
        this.username = checkValidUsername(username);
        this.password = checkValidPassword(password);
        this.role = role;
        this.active = active;
    }

    public static Usuario create(UUID id, String username, String password, Permissao permissao, boolean active) {
        return new Usuario(id, username, password, permissao, active);
    }

    private String checkValidUsername(String username) {
        checkNotBlank("username", username);
        checkSizeMax("username", USERNAME_SIZE_MAX, username);
        return username;
    }

    private String checkValidPassword(String password) {
        checkNotBlank("password", password);
        checkSizeMax("password", PASSWORD_SIZE_MAX, password);
        return password;
    }

    private void checkNotBlank(String fieldName, String value) {
        if (value == null || value.isBlank()) {
            throw new AllNullFieldsCustomException(fieldName);
        }
    }

    private void checkSizeMax(String fieldName, int sizeMax, String value) {
        if (value.length() > sizeMax) {
            throw new AttributeExceededMaximumLimitException(fieldName, String.valueOf(sizeMax));
        }
    }
}

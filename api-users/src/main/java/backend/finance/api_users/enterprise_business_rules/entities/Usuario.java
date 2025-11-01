package backend.finance.api_users.enterprise_business_rules.entities;

import backend.finance.api_users.application_business_rules.exception.http400.AllNullFieldsCustomException;
import backend.finance.api_users.application_business_rules.exception.http400.AttributeExceededMaximumLimitException;
import lombok.Getter;

import java.util.UUID;

import static backend.finance.api_users.enterprise_business_rules.constants.ConstantsValidation.PASSWORD_SIZE_MAX;
import static backend.finance.api_users.enterprise_business_rules.constants.ConstantsValidation.USERNAME_SIZE_MAX;

@Getter
public final class Usuario {

    private UUID id;

    private String username;

    private String password;

    private Permissao role;

    private boolean active;

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

    public void disable() {
        this.active = false;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = checkValidUsername(username);
    }

    public void setPassword(String password) {
        this.password = checkValidPassword(password);
    }

    public void setRole(Permissao role) {
        this.role = role;
    }
}

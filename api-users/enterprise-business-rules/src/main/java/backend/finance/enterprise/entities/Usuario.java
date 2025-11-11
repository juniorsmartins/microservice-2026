package backend.finance.enterprise.entities;

import backend.finance.enterprise.exceptions.AllNullFieldsCustomException;
import backend.finance.enterprise.exceptions.AttributeExceededMaximumLimitException;

import java.util.UUID;

import static backend.finance.enterprise.constants.ConstantsValidation.PASSWORD_SIZE_MAX;
import static backend.finance.enterprise.constants.ConstantsValidation.USERNAME_SIZE_MAX;

public final class Usuario {

    private final UUID id;

    private String username;

    private String password;

    private Permissao role;

    private boolean active;

    public Usuario(UUID id, String username, String password, Permissao role, boolean active) {
        this.id = id;
        this.username = checkValidUsername(username);
        this.password = checkValidPassword(password);
        this.role = role;
        this.active = active;
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

    //#region Getters
    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Permissao getRole() {
        return role;
    }

    public boolean isActive() {
        return active;
    }
    //#endregion Getters

    //#region Setters
    public void setUsername(String username) {
        this.username = checkValidUsername(username);
    }

    public void setPassword(String password) {
        this.password = checkValidPassword(password);
    }

    public void setRole(Permissao role) {
        this.role = role;
    }
    //#endregion Setters
}

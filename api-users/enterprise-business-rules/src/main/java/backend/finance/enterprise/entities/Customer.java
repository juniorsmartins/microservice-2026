package backend.finance.enterprise.entities;

import backend.finance.enterprise.exceptions.AllNullFieldsCustomException;
import backend.finance.enterprise.exceptions.AttributeExceededMaximumLimitException;
import backend.finance.enterprise.exceptions.EmailInvalidFormatCustomException;

import java.util.UUID;
import java.util.regex.Pattern;

import static backend.finance.enterprise.constants.ConstantsValidation.NAME_SIZE_MAX;

public final class Customer {

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    private final UUID id;

    private String name;

    private String email;

    private final Usuario user;

    private boolean active;

    public Customer(UUID id, String name, String email, Usuario usuario, boolean active) {
        this.id = id;
        this.name = validName(name);
        this.email = validEmail(email);
        this.user = usuario;
        this.active = active;
    }

    private String validName(String name) {
        checkNotBlank("name", name);
        checkSizeMax("name", NAME_SIZE_MAX, name);
        return name;
    }

    private String validEmail(String email) {
        checkNotBlank("email", email);
        checkFormatEmail(email);
        return email;
    }

    private void checkSizeMax(String fieldName, int sizeMax, String value) {
        if (value.length() > sizeMax) {
            throw new AttributeExceededMaximumLimitException(fieldName, String.valueOf(sizeMax));
        }
    }

    private void checkNotBlank(String fieldName, String value) {
        if (value == null || value.isBlank()) {
            throw new AllNullFieldsCustomException(fieldName);
        }
    }

    private void checkFormatEmail(String email) {
        if (!ehValido(email)) {
            throw new EmailInvalidFormatCustomException(email);
        }
    }

    private boolean ehValido(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    public void disable() {
        this.active = false;
    }

    //#region Getters
    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Usuario getUser() {
        return user;
    }

    public boolean isActive() {
        return active;
    }
    //#endregion Getters

    //#region Setters
    public void setName(String name) {
        this.name = validName(name);
    }

    public void setEmail(String email) {
        this.email = validEmail(email);
    }
    //#endregion
}

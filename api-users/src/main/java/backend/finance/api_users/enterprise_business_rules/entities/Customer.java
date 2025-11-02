package backend.finance.api_users.enterprise_business_rules.entities;

import backend.finance.api_users.application_business_rules.exception.http400.AllNullFieldsCustomException;
import backend.finance.api_users.application_business_rules.exception.http400.AttributeExceededMaximumLimitException;
import backend.finance.api_users.application_business_rules.exception.http400.EmailInvalidFormatCustomException;
import lombok.Getter;

import java.util.UUID;
import java.util.regex.Pattern;

import static backend.finance.api_users.enterprise_business_rules.constants.ConstantsValidation.NAME_SIZE_MAX;

@Getter
public final class Customer {

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    private UUID id;

    private String name;

    private String email;

    private Usuario user;

    private boolean active;

    private Customer(UUID id, String name, String email, Usuario usuario, boolean active) {
        this.id = id;
        this.name = validName(name);
        this.email = validEmail(email);
        this.user = usuario;
        this.active = active;
    }

    public static Customer create(UUID id, String name, String email, Usuario usuario, boolean active) {
        return new Customer(id, name, email, usuario, active);
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

    //#region Setters
    public void setId(UUID id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = validName(name);
    }

    public void setEmail(String email) {
        this.email = validEmail(email);
    }

    public void setUser(Usuario user) {
        this.user = user;
    }
    //#endregion
}

package backend.finance.api_user.domain.entities;

import backend.finance.api_user.application.configs.exception.http400.AllNullFieldsCustomException;
import backend.finance.api_user.application.configs.exception.http400.EmailInvalidFormatCustomException;
import backend.finance.api_user.application.dtos.input.CustomerRequest;
import backend.finance.api_user.application.dtos.internal.RoleDto;
import lombok.Getter;

import java.util.UUID;
import java.util.regex.Pattern;

@Getter
public final class Customer {

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    private final UUID id;

    private final String name;

    private final String email;

    private final Usuario user;

    private Customer(UUID id, String name, String email, Usuario user) {
        this.id = id;
        this.name = checkNotBlank("name", name);
        this.email = validateEmail(email);
        this.user = user;
    }

    public static Customer create(UUID id, CustomerRequest request, RoleDto roleDto) {
        var usuario = Usuario.create(request.user(), roleDto);
        return new Customer(id, request.name(), request.email(), usuario);
    }

    private String validateEmail(String email) {
        if (email == null || !ehValido(email)) {
            throw new EmailInvalidFormatCustomException(email);
        }
        return email;
    }

    private boolean ehValido(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    private String checkNotBlank(String fieldName, String value) {
        if (value == null || value.isBlank()) {
            throw new AllNullFieldsCustomException(fieldName);
        }
        return value;
    }
}

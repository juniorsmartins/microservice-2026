package backend.finance.api_account.domain.entities;

import backend.finance.api_account.application.utils.ValidationUtilities;
import jakarta.persistence.Column;
import lombok.Getter;

import java.util.UUID;

@Getter
public final class Usuario {

    private final UUID id;

    private final String name;

    private final String email;

    public Usuario(UUID id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}

package backend.finance.api_account.domain.entities;

import lombok.Getter;

import java.util.UUID;

@Getter
public final class Customer {

    private final UUID id;

    private final String name;

    private final String email;

    public Customer(UUID id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}

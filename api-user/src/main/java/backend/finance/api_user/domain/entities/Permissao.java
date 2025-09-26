package backend.finance.api_user.domain.entities;

import backend.finance.api_user.domain.enums.RoleEnum;
import lombok.Getter;

import java.util.UUID;

@Getter
public final class Permissao {

    private final UUID id;

    private final RoleEnum name;

    private Permissao(UUID id, RoleEnum name) {
        this.id = id;
        this.name = name;
    }

    public static Permissao create(UUID id, RoleEnum roleEnum) {
        return new Permissao(id, roleEnum);
    }
}

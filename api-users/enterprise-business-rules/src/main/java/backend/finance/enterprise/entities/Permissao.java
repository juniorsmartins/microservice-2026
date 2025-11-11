package backend.finance.enterprise.entities;

import backend.finance.enterprise.enums.RoleEnum;

import java.util.UUID;

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

    //#region Getters
    public UUID getId() {
        return id;
    }

    public RoleEnum getName() {
        return name;
    }
    //#endregion Getters
}

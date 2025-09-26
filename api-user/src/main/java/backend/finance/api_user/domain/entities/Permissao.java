package backend.finance.api_user.domain.entities;

import backend.finance.api_user.application.configs.exception.http404.RoleNotFoundCustomException;
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

    public static Permissao create(String role) {
        var roleEnum = getRole(role);
        return new Permissao(null, roleEnum);
    }

    private static RoleEnum getRole(String name) {
        try {
            return RoleEnum.valueOf(name);
        } catch (IllegalArgumentException e) {
            throw new RoleNotFoundCustomException(name);
        }
    }
}

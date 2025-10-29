package backend.finance.api_users.application.configs.exception.http404;

import java.io.Serial;

public final class RoleNotFoundCustomException extends ResourceNotFoundCustomException {

    @Serial
    private static final long serialVersionUID = 1L;

    public RoleNotFoundCustomException(final String roleName) {
        super("exception.resource.not-found.role", roleName);
    }
}

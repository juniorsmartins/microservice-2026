package backend.finance.api_users.application.configs.exception.http409;

import java.io.Serial;

public final class UsernameConflictRulesCustomException extends ResourceConflictRulesCustomException {

    @Serial
    private static final long serialVersionUID = 1L;

    public UsernameConflictRulesCustomException(final String username) {
        super("exception.resource.conflict.rules.username", username);
    }
}

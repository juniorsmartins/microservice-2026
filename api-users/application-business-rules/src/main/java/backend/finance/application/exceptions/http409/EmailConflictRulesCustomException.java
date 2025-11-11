package backend.finance.application.exceptions.http409;

import java.io.Serial;

public final class EmailConflictRulesCustomException extends ResourceConflictRulesCustomException {

    @Serial
    private static final long serialVersionUID = 1L;

    public EmailConflictRulesCustomException(final String email) {
        super("exception.resource.conflict.rules.email", email);
    }
}

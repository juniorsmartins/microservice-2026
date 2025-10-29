package backend.finance.api_users.application.configs.exception.http409;

import lombok.Getter;

import java.io.Serial;

@Getter
public abstract sealed class ResourceConflictRulesCustomException extends RuntimeException permits UsernameConflictRulesCustomException,
        EmailConflictRulesCustomException {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String messageKey;

    private final String value;

    protected ResourceConflictRulesCustomException(final String messageKey, final String value) {
        super(messageKey);
        this.messageKey = messageKey;
        this.value = value;
    }
}

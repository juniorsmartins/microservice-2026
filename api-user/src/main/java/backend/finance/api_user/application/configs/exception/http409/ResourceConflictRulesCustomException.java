package backend.finance.api_user.application.configs.exception.http409;

import lombok.Getter;

import java.io.Serial;
import java.util.UUID;

@Getter
public abstract class ResourceConflictRulesCustomException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String messageKey;

    private final UUID id;

    protected ResourceConflictRulesCustomException(final String messageKey, final UUID id) {
        super(messageKey);
        this.messageKey = messageKey;
        this.id = id;
    }
}

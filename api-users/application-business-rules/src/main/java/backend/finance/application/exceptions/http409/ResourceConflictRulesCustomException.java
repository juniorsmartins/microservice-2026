package backend.finance.application.exceptions.http409;

import java.io.Serial;

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

    //#region Getters
    public String getMessageKey() {
        return messageKey;
    }

    public String getValue() {
        return value;
    }
    //#endregion Getters
}

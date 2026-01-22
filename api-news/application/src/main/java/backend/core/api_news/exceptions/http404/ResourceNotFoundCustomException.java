package backend.core.api_news.exceptions.http404;

import java.io.Serial;

public abstract sealed class ResourceNotFoundCustomException extends RuntimeException permits NewsNotFoundCustomException {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String messageKey;

    private final String value;

    protected ResourceNotFoundCustomException(final String messageKey, final String value) {
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

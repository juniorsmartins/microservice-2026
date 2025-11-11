package backend.finance.enterprise.exceptions;

import java.io.Serial;

public abstract sealed class BadRequestCustomException extends RuntimeException permits EmailInvalidFormatCustomException,
        AllNullFieldsCustomException, AttributeExceededMaximumLimitException {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String messageKey;

    private final String value0;

    private final String value1;

    protected BadRequestCustomException(final String messageKey, final String value0) {
        super(messageKey);
        this.messageKey = messageKey;
        this.value0 = value0;
        this.value1 = null;
    }

    protected BadRequestCustomException(final String messageKey, final String value0, final String value1) {
        super(messageKey);
        this.messageKey = messageKey;
        this.value0 = value0;
        this.value1 = value1;
    }

    //#region Getters
    public String getMessageKey() {
        return messageKey;
    }

    public String getValue0() {
        return value0;
    }

    public String getValue1() {
        return value1;
    }
    //#endregion Getters
}

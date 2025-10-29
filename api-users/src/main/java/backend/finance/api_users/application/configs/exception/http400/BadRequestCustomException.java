package backend.finance.api_users.application.configs.exception.http400;

import lombok.Getter;

import java.io.Serial;

@Getter
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
}

package backend.finance.api_user.application.configs.exception.http400;

import lombok.Getter;

import java.io.Serial;

@Getter
public abstract sealed class BadRequestCustomException extends RuntimeException permits EmailInvalidFormatCustomException,
        AllNullFieldsCustomException {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String messageKey;

    private final String value;

    protected BadRequestCustomException(final String messageKey, final String value) {
        super(messageKey);
        this.messageKey = messageKey;
        this.value = value;
    }
}

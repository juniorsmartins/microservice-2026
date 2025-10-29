package backend.finance.api_users.application.configs.exception.http400;

import java.io.Serial;

public final class AttributeExceededMaximumLimitException extends BadRequestCustomException {

    @Serial
    private static final long serialVersionUID = 1L;

    public AttributeExceededMaximumLimitException(final String fieldName, final String sizeMax) {
        super("exception.field.exceeded-maximum-limit.request", fieldName, sizeMax);
    }
}

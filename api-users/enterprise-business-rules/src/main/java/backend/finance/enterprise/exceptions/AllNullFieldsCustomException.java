package backend.finance.enterprise.exceptions;

import java.io.Serial;

public final class AllNullFieldsCustomException extends BadRequestCustomException {

    @Serial
    private static final long serialVersionUID = 1L;

    public AllNullFieldsCustomException(final String fieldName) {
        super("exception.field.null.request.all", fieldName);
    }
}

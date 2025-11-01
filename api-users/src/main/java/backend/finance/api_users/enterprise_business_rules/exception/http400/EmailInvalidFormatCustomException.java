package backend.finance.api_users.enterprise_business_rules.exception.http400;

import java.io.Serial;

public final class EmailInvalidFormatCustomException extends BadRequestCustomException {

    @Serial
    private static final long serialVersionUID = 1L;

    public EmailInvalidFormatCustomException(final String email) {
        super("exception.poorly.formulated.request.email", email);
    }
}

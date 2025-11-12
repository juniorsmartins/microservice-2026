package backend.finance.application.exceptions.http400;

import java.io.Serial;

public final class EnterpriseValidationException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String messageKey;

    private final String value0;

    private final String value1;

    public EnterpriseValidationException(String messageKey, String value0, String value1) {
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

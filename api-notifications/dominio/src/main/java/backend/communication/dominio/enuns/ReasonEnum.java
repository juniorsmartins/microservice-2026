package backend.communication.dominio.enuns;

public enum ReasonEnum {

    CUSTOMER_CREATED("CUSTOMER_CREATED");

    private final String value;

    ReasonEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

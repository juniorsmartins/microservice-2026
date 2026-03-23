package backend.finance.enterprise.enums;

public enum RoleEnum {

    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_CUSTOMER("ROLE_CUSTOMER"),
    ROLE_USERS("ROLE_USERS"),
    ROLE_NEWS("ROLE_NEWS"),
    ROLE_IAS("ROLE_IAS"),
    ROLE_NOTIFICATIONS("ROLE_NOTIFICATIONS");

    private final String value;

    RoleEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

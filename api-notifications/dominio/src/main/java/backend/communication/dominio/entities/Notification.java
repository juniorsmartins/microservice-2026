package backend.communication.dominio.entities;

import backend.communication.dominio.enuns.ReasonEnum;

import java.time.OffsetDateTime;
import java.util.UUID;

public final class Notification {

    private final UUID id;

    private final UUID customerCode;

    private final String customerEmail;

    private final String message;

    private final ReasonEnum reason;

    private final OffsetDateTime createdAt;

    public Notification(UUID id, UUID customerCode, String customerEmail, String message, ReasonEnum reason, OffsetDateTime createdAt) {
        this.id = id;
        this.customerCode = customerCode;
        this.customerEmail = customerEmail;
        this.message = message;
        this.reason = reason;
        this.createdAt = createdAt;
    }

    public Notification(UUID customerCode, String customerEmail, String message, ReasonEnum reason, OffsetDateTime createdAt) {
        this(null, customerCode, customerEmail, message, reason, createdAt);
    }

    public Notification(UUID customerCode, String customerEmail, String message, ReasonEnum reason) {
        this(customerCode, customerEmail, message, reason, null);
    }

    public UUID getId() {
        return id;
    }

    public UUID getCustomerCode() {
        return customerCode;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public String getMessage() {
        return message;
    }

    public ReasonEnum getReason() {
        return reason;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}

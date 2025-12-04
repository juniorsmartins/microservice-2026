package backend.communication.infraestrutura.jpas;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "notifications")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@EqualsAndHashCode(of = {"id"})
public final class NotificationJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "customer_code", nullable = false)
    private UUID customerCode;

    @Column(name = "customer_email", nullable = false)
    private String customerEmail;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "reason", nullable = false)
    private String reason;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @PrePersist
    private void onCreate() {
        this.createdAt = OffsetDateTime.now();
    }
}

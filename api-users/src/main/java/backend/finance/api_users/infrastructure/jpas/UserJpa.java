package backend.finance.api_users.infrastructure.jpas;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

import static backend.finance.api_users.domain.constant.ConstantsValidation.PASSWORD_SIZE_MAX;
import static backend.finance.api_users.domain.constant.ConstantsValidation.USERNAME_SIZE_MAX;

@Entity
@Table(name = "users",
        indexes = {@Index(name = "idx_users_username", columnList = "username")}
)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@EqualsAndHashCode(of = {"username"})
@ToString
public final class UserJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "username", nullable = false, length = USERNAME_SIZE_MAX, unique = true)
    private String username;

    @Column(name = "password", nullable = false, length = PASSWORD_SIZE_MAX)
    private String password;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private RoleJpa role;

    @Column(name = "active", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean active;
}

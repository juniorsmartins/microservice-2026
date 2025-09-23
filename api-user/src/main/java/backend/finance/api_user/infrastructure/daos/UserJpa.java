package backend.finance.api_user.infrastructure.daos;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = {"username"})
@ToString
public final class UserJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "enabled", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean enabled;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private RoleJpa role;
}

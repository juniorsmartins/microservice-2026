package backend.finance.api_user.infrastructure.jpas;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

import static backend.finance.api_user.domain.constant.ConstantsValidation.EMAIL_SIZE_MAX;
import static backend.finance.api_user.domain.constant.ConstantsValidation.NAME_SIZE_MAX;

@Entity
@Table(name = "customers")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@EqualsAndHashCode(of = {"email"})
@ToString
public final class CustomerJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false, length = NAME_SIZE_MAX)
    private String name;

    @Column(name = "email", nullable = false, length = EMAIL_SIZE_MAX, unique = true)
    private String email;

    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, optional = false, orphanRemoval = true)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserJpa user;
}

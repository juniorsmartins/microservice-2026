package backend.finance.adapters.jpas;

import backend.finance.adapters.jpas.auditoria.AbstractAuditingJpa;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Audited
@Entity
@Table(name = "roles", indexes = {@Index(name = "idx_roles_name", columnList = "name")})
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = {"name"}, callSuper = false)
@ToString
public final class RoleJpa extends AbstractAuditingJpa implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;
}

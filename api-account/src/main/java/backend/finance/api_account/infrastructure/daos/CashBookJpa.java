package backend.finance.api_account.infrastructure.daos;

import jakarta.persistence.*;
import lombok.*;

import java.time.Year;
import java.util.UUID;

@Entity
@Table(name = "cash_books", uniqueConstraints = {
        @UniqueConstraint(name = "UK_account_year", columnNames = {"account_id", "year"})
}) // Garantir que um livro caixa é único por conta e ano
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@EqualsAndHashCode(of = {"account", "year"})
public final class CashBookJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "year", nullable = false)
    private Year year;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    private AccountJpa account;
}

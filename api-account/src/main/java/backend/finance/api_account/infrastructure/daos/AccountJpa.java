package backend.finance.api_account.infrastructure.daos;

import backend.finance.api_account.domain.enums.AccountTypeEnum;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "accounts")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA exige um construtor sem argumentos. force = true inicializa campos finais com valores padrão
@Getter
@Setter
@EqualsAndHashCode(of = {"number"})
public final class AccountJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // TODO - Chave única composta com o Usuário
    @Column(name = "number", nullable = false, unique = true)
    private Long number;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    private AccountTypeEnum accountType;

    @Column(name = "current_balance", nullable = false, precision = 19, scale = 3) // Saldo atual da conta
    private BigDecimal currentBalance;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private CustomerJpa usuario;
}

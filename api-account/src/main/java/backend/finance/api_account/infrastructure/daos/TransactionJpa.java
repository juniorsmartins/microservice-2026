package backend.finance.api_account.infrastructure.daos;

import backend.finance.api_account.domain.enums.CostCenterEnum;
import backend.finance.api_account.domain.enums.TransactionTypeEnum;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "transactions")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
public final class TransactionJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // TODO - O amount deve ser positivo se o tipo for RECEITA e negativo se for DESPESA (validation e usecase)
    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionTypeEnum transactionType;

    @Enumerated(EnumType.STRING)
    @Column(name = "cost_center", nullable = false)
    private CostCenterEnum costCenter;

    @Column(name = "amount", nullable = false, precision = 19, scale = 3)
    private BigDecimal amount;

    private String description;

    private String supplier;

    @Column(name = "date_operation", nullable = false)
    private LocalDate dateOperation;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cash_book_id", nullable = false)
    private CashBookJpa cashBook;
}

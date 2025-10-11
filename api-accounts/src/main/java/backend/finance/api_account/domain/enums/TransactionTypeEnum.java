package backend.finance.api_account.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TransactionTypeEnum {

    INCOME("INCOME"), // Receita
    EXPENSE("EXPENSE"), // Despesa
    CHARGEBACK("CHARGEBACK"), // Estorno
    TRANSFER("TRANSFER"); // Transferência

    private final String value;
}

package backend.finance.api_account.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AccountTypeEnum {

    CONTA_CORRENTE("CONTA_CORRENTE"),
    CONTA_POUPANCA("CONTA_POUPANCA"),
    CONTA_SALARIO("CONTA_SALARIO"),
    CONTA_INVESTIMENTO("CONTA_INVESTIMENTO"),
    CONTA_INTERNACIONAL("CONTA_INTERNACIONAL");

    private final String value;
}

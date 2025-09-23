package backend.finance.api_account.domain.entities;

import backend.finance.api_account.application.utils.ValidationUtilities;
import backend.finance.api_account.domain.enums.AccountTypeEnum;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
public final class Account {

    private final UUID id;

    private final Long number;

    private final AccountTypeEnum accountType;

    private final BigDecimal currentBalance;

    private final Usuario usuario;

    public Account(String id, Long number, String accountType, BigDecimal currentBalance, Usuario usuario) {
        this.id = ValidationUtilities.validateId(id);
        this.number = number;
        this.accountType = validateAccountType(accountType);
        this.currentBalance = currentBalance;
        this.usuario = usuario;
    }

    public Account(UUID id, Long number, AccountTypeEnum accountType, BigDecimal currentBalance, Usuario usuario) {
        this.id = id;
        this.number = number;
        this.accountType = accountType;
        this.currentBalance = currentBalance;
        this.usuario = usuario;
    }

    private AccountTypeEnum validateAccountType(String accountType) {
        try {
            return AccountTypeEnum.valueOf(accountType);
        } catch (Exception e) {
            // TODO - Add Custom Exception
            throw new IllegalArgumentException("Invalid account type: " + accountType);
        }
    }
}

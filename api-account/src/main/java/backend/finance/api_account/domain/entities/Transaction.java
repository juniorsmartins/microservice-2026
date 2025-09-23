package backend.finance.api_account.domain.entities;

import backend.finance.api_account.application.utils.ValidationUtilities;
import backend.finance.api_account.domain.enums.CostCenterEnum;
import backend.finance.api_account.domain.enums.TransactionTypeEnum;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
public final class Transaction {

    private final UUID id;

    private final TransactionTypeEnum transactionType;

    private final CostCenterEnum costCenter;

    private final BigDecimal amount;

    private final String description;

    private final String supplier;

    private final LocalDate dateOperation;

    private final CashBook cashBook;

    public Transaction(String id, String transactionType, String costCenter, BigDecimal amount, String description, String supplier, String dateOperation, CashBook cashBook) {
        this.id = ValidationUtilities.validateId(id);
        this.transactionType = validateTransactionType(transactionType);
        this.costCenter = validateCostCenter(costCenter);
        this.amount = amount;
        this.description = description;
        this.supplier = supplier;
        this.dateOperation = ValidationUtilities.validateLocalDate(dateOperation);
        this.cashBook = cashBook;
    }

    public Transaction(UUID id, TransactionTypeEnum transactionType, CostCenterEnum costCenter, BigDecimal amount, String description, String supplier, LocalDate dateOperation, CashBook cashBook) {
        this.id = id;
        this.transactionType = transactionType;
        this.costCenter = costCenter;
        this.amount = amount;
        this.description = description;
        this.supplier = supplier;
        this.dateOperation = dateOperation;
        this.cashBook = cashBook;
    }

    private TransactionTypeEnum validateTransactionType(String transactionType) {
        try {
            return TransactionTypeEnum.valueOf(transactionType);
        } catch (Exception e) {
            // TODO - Add Custom Exception
            throw new IllegalArgumentException("Invalid transaction type: " + transactionType);
        }
    }

    private CostCenterEnum validateCostCenter(String costCenter) {
        try {
            return CostCenterEnum.valueOf(costCenter);
        } catch (Exception e) {
            // TODO - Add Custom Exception
            throw new IllegalArgumentException("Invalid cost center: " + costCenter);
        }
    }
}

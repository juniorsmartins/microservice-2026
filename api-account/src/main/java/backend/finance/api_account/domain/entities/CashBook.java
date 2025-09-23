package backend.finance.api_account.domain.entities;

import backend.finance.api_account.application.utils.ValidationUtilities;
import lombok.Getter;

import java.time.Year;
import java.util.UUID;

@Getter
public final class CashBook {

    private final UUID id;

    private final Year year;

    private final Account account;

    public CashBook(String id, int year, Account account) {
        this.id = ValidationUtilities.validateId(id);
        this.year = validateYear(year);
        this.account = account;
    }

    public CashBook(UUID id, Year year, Account account) {
        this.id = id;
        this.year = year;
        this.account = account;
    }

    private Year validateYear(int year) {
        try {
            return Year.of(year);
        } catch (Exception e) {
            // TODO - Add Custom Exception
            throw new IllegalArgumentException("Invalid year: " + year);
        }
    }
}

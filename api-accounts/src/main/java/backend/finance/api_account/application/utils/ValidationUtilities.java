package backend.finance.api_account.application.utils;

import java.time.LocalDate;
import java.util.UUID;

public final class ValidationUtilities {

    public static UUID validateId(String id) {
        if (id == null || id.isEmpty()) {
            // TODO - Add Custom Exception
            throw new IllegalArgumentException("ID cannot be null or empty");
        }

        try {
            return UUID.fromString(id);
        } catch (Exception e) {
            // TODO - Add Custom Exception
            throw new IllegalArgumentException("Invalid format UUID string: " + id);
        }
    }

    public static LocalDate validateLocalDate(String date) {
        if (date == null || date.isEmpty()) {
            // TODO - Add Custom Exception
            throw new IllegalArgumentException("Date cannot be null or empty");
        }

        try {
            return LocalDate.parse(date);
        } catch (Exception e) {
            // TODO - Add Custom Exception
            throw new IllegalArgumentException("Invalid format LocalDate string: " + date);
        }
    }
}

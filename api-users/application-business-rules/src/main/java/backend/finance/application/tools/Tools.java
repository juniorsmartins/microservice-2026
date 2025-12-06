package backend.finance.application.tools;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class Tools {

    // Formata LocalDate para ISO com DateTimeFormatter
    public String formatLocalDateToIso(LocalDate localDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        return localDate.atStartOfDay().atOffset(ZoneOffset.UTC).format(formatter);
    }
}

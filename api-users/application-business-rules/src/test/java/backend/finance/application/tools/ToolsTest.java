package backend.finance.application.tools;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ToolsTest {

    @Test
    void dadoLocalDate_whenFormatLocalDateToIso_thenISOFormatOk(){
        Tools tools = new Tools();
        LocalDate localDate = LocalDate.of(2023, 11, 6);

        String expected = "2023-11-06T00:00:00.000Z";
        String actual = tools.formatLocalDateToIso(localDate);
        assertEquals(expected, actual);
    }
}
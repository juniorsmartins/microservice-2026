package backend.finance.application.ports.input;

import java.util.UUID;

public interface CustomerDisableInputPort {

    void disableById(UUID id);
}

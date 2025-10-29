package backend.finance.api_users.infrastructure.ports.input;

import java.util.UUID;

public interface CustomerDeleteInputPort {

    void disableById(UUID id);
}

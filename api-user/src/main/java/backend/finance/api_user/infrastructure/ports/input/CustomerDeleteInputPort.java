package backend.finance.api_user.infrastructure.ports.input;

import java.util.UUID;

public interface CustomerDeleteInputPort {

    void deleteById(UUID id);
}

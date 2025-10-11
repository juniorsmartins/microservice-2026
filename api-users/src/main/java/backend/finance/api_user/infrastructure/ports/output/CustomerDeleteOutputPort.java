package backend.finance.api_user.infrastructure.ports.output;

import java.util.UUID;

public interface CustomerDeleteOutputPort {

    void deleteById(UUID id);
}

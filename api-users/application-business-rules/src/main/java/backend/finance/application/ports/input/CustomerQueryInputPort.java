package backend.finance.application.ports.input;

import backend.finance.application.dtos.response.CustomerResponse;

import java.util.UUID;

public interface CustomerQueryInputPort {

    CustomerResponse findActiveById(UUID id);
}

package backend.finance.application.ports.output;

import backend.finance.application.dtos.CustomerDto;

public interface CustomerSaveOutputPort {

    CustomerDto save(CustomerDto dto);
}

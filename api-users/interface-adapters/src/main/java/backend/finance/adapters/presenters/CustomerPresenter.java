package backend.finance.adapters.presenters;

import backend.finance.adapters.CustomerMessage;
import backend.finance.adapters.jpas.CustomerJpa;
import backend.finance.application.dtos.CustomerDto;
import backend.finance.application.dtos.response.CustomerResponse;

public interface CustomerPresenter {

    CustomerResponse toResponse(CustomerJpa jpa);

    CustomerMessage toMessage(CustomerResponse response);

    CustomerDto toDto(CustomerJpa jpa);

    CustomerJpa toJpa(CustomerDto dto);
}

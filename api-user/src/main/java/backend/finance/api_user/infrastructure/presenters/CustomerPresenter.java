package backend.finance.api_user.infrastructure.presenters;

import backend.finance.api_user.application.dtos.internal.CustomerDto;
import backend.finance.api_user.application.dtos.output.CustomerResponse;
import backend.finance.api_user.domain.entities.Customer;
import backend.finance.api_user.infrastructure.jpas.CustomerJpa;

public interface CustomerPresenter {

    CustomerResponse toCustomerResponse(CustomerDto dto);

    CustomerDto toCustomerDto(CustomerJpa jpa);

    CustomerJpa toCustomerJpa(Customer customer);
}

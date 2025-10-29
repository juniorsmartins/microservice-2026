package backend.finance.api_user.infrastructure.presenters;

import backend.finance.api.users.CustomerMessage;
import backend.finance.api_user.application.dtos.output.CustomerResponse;
import backend.finance.api_user.domain.entities.Customer;
import backend.finance.api_user.infrastructure.jpas.CustomerJpa;

public interface CustomerPresenter {

    CustomerResponse toResponse(Customer customer);

    CustomerJpa toJpa(Customer customer);

    Customer toEntity(CustomerJpa jpa);

    CustomerMessage toMessage(CustomerResponse response);
}

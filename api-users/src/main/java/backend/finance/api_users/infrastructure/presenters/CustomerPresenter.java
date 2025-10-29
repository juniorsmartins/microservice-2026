package backend.finance.api_users.infrastructure.presenters;

import backend.finance.api_users.application.configs.mensageria.CustomerMessage;
import backend.finance.api_users.application.dtos.output.CustomerResponse;
import backend.finance.api_users.domain.entities.Customer;
import backend.finance.api_users.infrastructure.jpas.CustomerJpa;

public interface CustomerPresenter {

    CustomerResponse toResponse(Customer customer);

    CustomerJpa toJpa(Customer customer);

    Customer toEntity(CustomerJpa jpa);

    CustomerMessage toMessage(CustomerResponse response);
}

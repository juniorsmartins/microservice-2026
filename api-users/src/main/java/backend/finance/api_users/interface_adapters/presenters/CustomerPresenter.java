package backend.finance.api_users.interface_adapters.presenters;

import backend.finance.api_users.CustomerMessage;
import backend.finance.api_users.application_business_rules.dtos.output.CustomerResponse;
import backend.finance.api_users.enterprise_business_rules.entities.Customer;
import backend.finance.api_users.interface_adapters.jpas.CustomerJpa;

public interface CustomerPresenter {

    CustomerResponse toResponse(Customer customer);

    CustomerJpa toJpa(Customer customer);

    Customer toEntity(CustomerJpa jpa);

    CustomerMessage toMessage(Customer customer);
}

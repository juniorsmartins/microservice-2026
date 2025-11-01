package backend.finance.api_users.interface_adapters.presenters;

import backend.finance.api_users.application.configs.mensageria.CustomerMessage;
import backend.finance.api_users.application_business_rules.dtos.output.CustomerResponse;
import backend.finance.api_users.enterprise_business_rules.entities.Customer;
import backend.finance.api_users.frameworks_drivers.jpas.CustomerJpa;

public interface CustomerPresenter {

    CustomerResponse toResponse(Customer customer);

    CustomerJpa toJpa(Customer customer);

    Customer toEntity(CustomerJpa jpa);

    CustomerMessage toMessage(CustomerResponse response);
}

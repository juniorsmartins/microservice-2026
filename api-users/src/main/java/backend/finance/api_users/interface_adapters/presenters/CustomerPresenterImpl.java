package backend.finance.api_users.interface_adapters.presenters;

import backend.finance.api_users.CustomerMessage;
import backend.finance.api_users.application_business_rules.dtos.output.CustomerResponse;
import backend.finance.api_users.enterprise_business_rules.entities.Customer;
import backend.finance.api_users.interface_adapters.jpas.CustomerJpa;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class CustomerPresenterImpl implements CustomerPresenter {

    private final UserPresenter userPresenter;

    @Override
    public CustomerResponse toResponse(Customer customer) {
        var userResponse = userPresenter.toResponse(customer.getUser());
        return new CustomerResponse(customer.getId(), customer.getName(), customer.getEmail(), userResponse, customer.isActive());
    }

    @Override
    public CustomerResponse toResponse(CustomerJpa jpa) {
        var userResponse = userPresenter.toResponse(jpa.getUser());
        return new CustomerResponse(jpa.getId(), jpa.getName(), jpa.getEmail(), userResponse, jpa.isActive());
    }

    @Override
    public CustomerJpa toJpa(Customer customer) {
        var userJpa = userPresenter.toJpa(customer.getUser());
        return new CustomerJpa(customer.getId(), customer.getName(), customer.getEmail(), userJpa, customer.isActive());
    }

    @Override
    public Customer toEntity(CustomerJpa jpa) {
        var usuario = userPresenter.toEntity(jpa.getUser());
        return Customer.create(jpa.getId(), jpa.getName(), jpa.getEmail(), usuario, jpa.isActive());
    }

    @Override
    public CustomerMessage toMessage(CustomerResponse response) {
        return new CustomerMessage(response.id().toString(), response.name(), response.email());
    }
}

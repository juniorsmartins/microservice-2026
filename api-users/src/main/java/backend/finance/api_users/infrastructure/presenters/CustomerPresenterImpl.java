package backend.finance.api_users.infrastructure.presenters;

import backend.finance.api_users.application.configs.mensageria.CustomerMessage;
import backend.finance.api_users.application.dtos.output.CustomerResponse;
import backend.finance.api_users.domain.entities.Customer;
import backend.finance.api_users.infrastructure.jpas.CustomerJpa;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class CustomerPresenterImpl implements CustomerPresenter {

    private final UserPresenter userPresenter;

    @Override
    public CustomerResponse toResponse(Customer customer) {
        var userResponse = userPresenter.toUserResponse(customer.getUser());
        return new CustomerResponse(customer.getId(), customer.getName(), customer.getEmail(), userResponse, customer.isActive());
    }

    @Override
    public CustomerJpa toJpa(Customer customer) {
        var userJpa = userPresenter.toUserJpa(customer.getUser());
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

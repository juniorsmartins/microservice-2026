package backend.finance.api_user.infrastructure.presenters;

import backend.finance.api_user.application.dtos.internal.CustomerDto;
import backend.finance.api_user.application.dtos.output.CustomerResponse;
import backend.finance.api_user.domain.entities.Customer;
import backend.finance.api_user.infrastructure.jpas.CustomerJpa;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class CustomerPresenterImpl implements CustomerPresenter {

    private final UserPresenter userPresenter;

    @Override
    public CustomerResponse toCustomerResponse(CustomerDto dto) {
        var userResponse = userPresenter.toUserResponse(dto.user());
        return new CustomerResponse(dto.id(), dto.name(), dto.email(), userResponse);
    }

    @Override
    public CustomerDto toCustomerDto(CustomerJpa jpa) {
        var userDto = userPresenter.toUserDto(jpa.getUser());
        return new CustomerDto(jpa.getId(), jpa.getName(), jpa.getEmail(), userDto);
    }

    @Override
    public CustomerJpa toCustomerJpa(Customer customer) {
        var userJpa = userPresenter.toUserJpa(customer.getUser());
        return new CustomerJpa(customer.getId(), customer.getName(), customer.getEmail(), userJpa);
    }
}

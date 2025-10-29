package backend.finance.api_user.infrastructure.presenters;

import backend.finance.api.users.CustomerMessage;
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
    public CustomerResponse toResponse(CustomerDto dto) {
        var userResponse = userPresenter.toUserResponse(dto.user());
        return new CustomerResponse(dto.id(), dto.name(), dto.email(), userResponse);
    }

    @Override
    public CustomerDto toDto(CustomerJpa jpa) {
        var userDto = userPresenter.toUserDto(jpa.getUser());
        return new CustomerDto(jpa.getId(), jpa.getName(), jpa.getEmail(), userDto);
    }

    @Override
    public CustomerJpa toJpa(Customer customer) {
        var userJpa = userPresenter.toUserJpa(customer.getUser());
        return new CustomerJpa(customer.getId(), customer.getName(), customer.getEmail(), userJpa);
    }

    @Override
    public CustomerMessage toMessage(CustomerResponse response) {
        return new CustomerMessage(response.id().toString(), response.name(), response.email());
    }
}

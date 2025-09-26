package backend.finance.api_user.infrastructure.presenters;

import backend.finance.api_user.application.dtos.input.CustomerRequest;
import backend.finance.api_user.application.dtos.internal.CustomerDto;
import backend.finance.api_user.application.dtos.output.CustomerResponse;
import backend.finance.api_user.infrastructure.jpas.CustomerJpa;
import backend.finance.api_user.infrastructure.jpas.RoleJpa;
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
        var useDto = userPresenter.toUserDto(jpa.getUser());
        return new CustomerDto(jpa.getId(), jpa.getName(), jpa.getEmail(), useDto);
    }

    @Override
    public CustomerJpa toCustomerJpa(CustomerRequest request, RoleJpa role) {
        var userJpa = userPresenter.toUserJpa(request.user(), role);
        return new CustomerJpa(null, request.name(), request.email(), userJpa);
    }
}

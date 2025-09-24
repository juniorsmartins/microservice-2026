package backend.finance.api_user.infrastructure.presenters;

import backend.finance.api_user.application.dtos.input.CustomerRequest;
import backend.finance.api_user.application.dtos.internal.CustomerDto;
import backend.finance.api_user.application.dtos.output.CustomerResponse;
import backend.finance.api_user.infrastructure.jpas.CustomerJpa;
import backend.finance.api_user.infrastructure.jpas.RoleJpa;

public final class CustomerPresenter {

    public static CustomerResponse toCustomerResponse(CustomerDto dto) {
        var userResponse = UserPresenter.toUserResponse(dto.user());
        return new CustomerResponse(dto.id(), dto.name(), dto.email(), userResponse);
    }

    public static CustomerDto toCustomerDto(CustomerJpa jpa) {
        var useDto = UserPresenter.toUserDto(jpa.getUser());
        return new CustomerDto(jpa.getId(), jpa.getName(), jpa.getEmail(), useDto);
    }

    public static CustomerJpa toCustomerJpa(CustomerRequest request, RoleJpa role) {
        var userJpa = UserPresenter.toUserJpa(request.user(), role);
        return new CustomerJpa(null, request.name(), request.email(), userJpa);
    }
}

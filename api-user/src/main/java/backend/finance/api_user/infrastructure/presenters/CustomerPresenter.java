package backend.finance.api_user.infrastructure.presenters;

import backend.finance.api_user.application.dtos.internal.CustomerDto;
import backend.finance.api_user.application.dtos.output.CustomerResponse;

public final class CustomerPresenter {

    public static CustomerResponse toCustomerResponse(CustomerDto dto) {
        var userResponse = UserPresenter.toUserResponse(dto.user());
        return new CustomerResponse(dto.id(), dto.name(), dto.email(), userResponse);
    }
}

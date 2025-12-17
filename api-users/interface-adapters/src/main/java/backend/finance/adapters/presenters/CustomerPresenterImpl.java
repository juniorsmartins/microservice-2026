package backend.finance.adapters.presenters;

import backend.CustomerMessage;
import backend.finance.adapters.jpas.CustomerJpa;
import backend.finance.application.dtos.CustomerDto;
import backend.finance.application.dtos.response.CustomerAllResponse;
import backend.finance.application.dtos.response.CustomerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class CustomerPresenterImpl implements CustomerPresenter {

    private final UserPresenter userPresenter;

    @Override
    public CustomerResponse toResponse(CustomerJpa jpa) {
        var userResponse = userPresenter.toResponse(jpa.getUser());
        return new CustomerResponse(jpa.getId(), jpa.getName(), jpa.getEmail(), userResponse, jpa.isActive());
    }

    @Override
    public CustomerMessage toMessage(CustomerResponse response) {
        return new CustomerMessage(response.id().toString(), response.name(), response.email());
    }

    @Override
    public CustomerDto toDto(CustomerJpa jpa) {
        var userDto = userPresenter.toDto(jpa.getUser());
        return new CustomerDto(jpa.getId(), jpa.getName(), jpa.getEmail(), userDto, jpa.isActive());
    }

    @Override
    public CustomerJpa toJpa(CustomerDto dto) {
        var userJpa = userPresenter.toJpa(dto.user());
        return new CustomerJpa(dto.id(), dto.name(), dto.email(), userJpa, dto.active());
    }

    @Override
    public CustomerAllResponse toAllResponse(CustomerJpa jpa) {
        var userAllResponse = userPresenter.toAllResponse(jpa.getUser());
        return new CustomerAllResponse(jpa.getId(), jpa.getName(), jpa.getEmail(), userAllResponse, jpa.isActive(),
                jpa.getCreatedBy(), jpa.getLastModifiedBy(), jpa.getCreatedDate(), jpa.getLastModifiedDate());
    }
}

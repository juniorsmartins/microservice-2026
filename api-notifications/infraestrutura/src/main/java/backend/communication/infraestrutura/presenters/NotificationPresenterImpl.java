package backend.communication.infraestrutura.presenters;

import backend.communication.aplicacao.dtos.NotificationDto;
import backend.communication.infraestrutura.dtos.responses.NotificationResponse;
import backend.communication.infraestrutura.jpas.NotificationJpa;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationPresenterImpl implements NotificationPresenter {

    @Override
    public NotificationJpa toJpa(NotificationDto dto) {
        return new NotificationJpa(dto.id(), dto.customerCode(), dto.customerEmail(), dto.message(), dto.reason(), dto.createdAt());
    }

    @Override
    public NotificationDto toDto(NotificationJpa jpa) {
        return new NotificationDto(jpa.getId(), jpa.getCustomerCode(), jpa.getCustomerEmail(), jpa.getMessage(), jpa.getReason(), jpa.getCreatedAt());
    }

    @Override
    public NotificationResponse toResponse(NotificationDto dto) {
        return new NotificationResponse(dto.id(), dto.customerCode(), dto.customerEmail(), dto.message(), dto.reason(), dto.createdAt());
    }

    @Override
    public NotificationResponse toResponse(NotificationJpa jpa) {
        return new NotificationResponse(jpa.getId(), jpa.getCustomerCode(), jpa.getCustomerEmail(), jpa.getMessage(), jpa.getReason(), jpa.getCreatedAt());
    }
}

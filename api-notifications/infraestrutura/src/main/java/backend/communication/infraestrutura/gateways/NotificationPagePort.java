package backend.communication.infraestrutura.gateways;

import backend.communication.infraestrutura.dtos.responses.NotificationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationPagePort {

    Page<NotificationResponse> pageAll(Pageable paginacao);
}

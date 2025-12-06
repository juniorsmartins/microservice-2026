package backend.communication.infraestrutura.gateways;

import backend.communication.aplicacao.dtos.response.NotificationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationPagePort {

    Page<NotificationResponse> pageAll(Pageable paginacao);
}

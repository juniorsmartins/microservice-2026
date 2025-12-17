package backend.finance.adapters.gateways;

import backend.finance.application.dtos.response.CustomerAllResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerPagePort {

    Page<CustomerAllResponse> pageAll(Pageable paginacao);
}

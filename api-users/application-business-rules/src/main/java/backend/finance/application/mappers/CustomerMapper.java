package backend.finance.application.mappers;

import backend.finance.application.dtos.CustomerDto;
import backend.finance.application.dtos.response.CustomerResponse;
import backend.finance.enterprise.entities.Customer;

public interface CustomerMapper {

    CustomerDto toDto(Customer customer);

    Customer toEntity(CustomerDto dto);

    CustomerResponse toResponse(CustomerDto dto);
}

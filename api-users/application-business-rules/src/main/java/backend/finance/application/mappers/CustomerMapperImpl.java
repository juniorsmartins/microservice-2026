package backend.finance.application.mappers;

import backend.finance.application.dtos.CustomerDto;
import backend.finance.application.dtos.response.CustomerResponse;
import backend.finance.enterprise.entities.Customer;

public final class CustomerMapperImpl implements CustomerMapper {

    private final UserMapper userMapper;

    public CustomerMapperImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public CustomerDto toDto(Customer customer) {
        var userDto = userMapper.toDto(customer.getUser());
        return new CustomerDto(customer.getId(), customer.getName(), customer.getEmail(), userDto,  customer.isActive());
    }

    @Override
    public Customer toEntity(CustomerDto dto) {
        var usuario = userMapper.toEntity(dto.user());
        return new Customer(dto.id(), dto.name(), dto.email(), usuario, dto.active());
    }

    @Override
    public CustomerResponse toResponse(CustomerDto dto) {
        var userResponse = userMapper.toResponse(dto.user());
        return new CustomerResponse(dto.id(), dto.name(), dto.email(), userResponse, dto.active());
    }
}

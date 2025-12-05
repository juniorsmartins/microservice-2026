package backend.finance.application.Utils;

import backend.finance.application.dtos.CustomerDto;
import backend.finance.application.dtos.RoleDto;
import backend.finance.application.dtos.UserDto;
import backend.finance.application.dtos.request.CustomerRequest;
import backend.finance.application.dtos.request.UserRequest;
import backend.finance.enterprise.enums.RoleEnum;

import java.util.UUID;

public class CustomerTestFactory {

    private CustomerTestFactory() {}

    public static CustomerRequest buildRequest(String username, String password, String role,
                                               String name, String email) {

        var userRequest = trainUserRequest(username, password, role);
        return trainRequest(name, email, userRequest);
    }

    public static CustomerRequest defaultRequest() {
        return buildRequest("username-teste", "password123", RoleEnum.ROLE_CUSTOMER.getValue(),
                "Anne Frank", "teste@email.com"
        );
    }

    public static CustomerDto buildDto(String username, String password, RoleEnum role,
                                               String name, String email) {

        var userDto = trainUserDto(username, password, role);
        return trainDto(name, email, userDto);
    }

    public static CustomerDto defaultDto() {
        return buildDto("username", "password", RoleEnum.ROLE_CUSTOMER,
                "Anne Frank", "teste@email.com"
        );
    }

    private static CustomerRequest trainRequest(String name, String email, UserRequest user) {
        return new CustomerRequest(name, email, user);
    }

    private static UserRequest trainUserRequest(String username, String password, String role) {
        return new UserRequest(username, password, role);
    }

    private static CustomerDto trainDto(String name, String email, UserDto user) {
        return new CustomerDto(UUID.randomUUID(), name, email, user, true);
    }

    private static UserDto trainUserDto(String username, String password, RoleEnum role) {
        var roleDto = new RoleDto(UUID.randomUUID(), role.name());
        return new UserDto(UUID.randomUUID(), username, password, roleDto, true);
    }
}

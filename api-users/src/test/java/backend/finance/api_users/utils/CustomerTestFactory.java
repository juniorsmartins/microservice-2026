package backend.finance.api_users.utils;

import backend.finance.api_users.application_business_rules.dtos.input.CustomerRequest;
import backend.finance.api_users.enterprise_business_rules.enums.RoleEnum;

public class CustomerTestFactory {

    private CustomerTestFactory() {}

    public static CustomerRequest buildRequest(
            String username, String password, String role, String name, String email) {

        var userRequest = UserTestUtils.trainRequest(username, password, role);
        return CustomerTestUtils.trainRequest(name, email, userRequest);
    }

    public static CustomerRequest defaultRequest() {
        return buildRequest("username-teste", "password123", RoleEnum.ROLE_CUSTOMER.getValue(),
                "Anne Frank", "teste@email.com"
        );
    }
}

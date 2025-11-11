package backend.finance.adapters.utils;

import backend.finance.application.dtos.request.CustomerRequest;

public class CustomerTestFactory {

    private CustomerTestFactory() {}

    public static CustomerRequest buildRequest(
            String username, String password, String role, String name, String email) {

        var userRequest = UserTestUtils.trainRequest(username, password, role);
        return CustomerTestUtils.trainRequest(name, email, userRequest);
    }

    public static CustomerRequest defaultRequest() {
        return buildRequest("username123", "password123", "ROLE_CUSTOMER",
                "Anne Teste Frank", "aneteste@email.com"
        );
    }
}

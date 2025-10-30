package backend.finance.api_users.utils;

import backend.finance.api_users.application.dtos.input.CustomerRequest;
import backend.finance.api_users.application.dtos.input.UserRequest;

public class CustomerTestUtils {

    public static CustomerRequest trainRequest(String name, String email, UserRequest user) {
        return new CustomerRequest(name, email, user);
    }
}

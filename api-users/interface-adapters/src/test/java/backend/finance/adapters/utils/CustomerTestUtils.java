package backend.finance.adapters.utils;

import backend.finance.application.dtos.request.CustomerRequest;
import backend.finance.application.dtos.request.UserRequest;

public class CustomerTestUtils {

    public static CustomerRequest trainRequest(String name, String email, UserRequest user) {
        return new CustomerRequest(name, email, user);
    }
}

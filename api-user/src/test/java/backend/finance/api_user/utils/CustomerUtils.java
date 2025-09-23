package backend.finance.api_user.utils;

import backend.finance.api_user.application.dtos.input.CustomerRequest;
import backend.finance.api_user.application.dtos.input.UserRequest;

public class CustomerUtils {

    public static CustomerRequest trainCustomerRequest(String name, String email, UserRequest user) {
        return new CustomerRequest(name, email, user);
    }
}

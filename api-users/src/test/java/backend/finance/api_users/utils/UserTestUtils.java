package backend.finance.api_users.utils;

import backend.finance.api_users.application_business_rules.dtos.input.UserRequest;

public class UserTestUtils {

    public static UserRequest trainRequest(String username, String password, String role) {
        return new UserRequest(username, password, role);
    }
}

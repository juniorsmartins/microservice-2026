package backend.finance.adapters.utils;

import backend.finance.application.dtos.request.UserRequest;

public class UserTestUtils {

    public static UserRequest trainRequest(String username, String password, String role) {
        return new UserRequest(username, password, role);
    }
}

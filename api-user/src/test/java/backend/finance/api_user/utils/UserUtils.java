package backend.finance.api_user.utils;

import backend.finance.api_user.application.dtos.input.UserRequest;

public class UserUtils {

    public static UserRequest trainUserRequest(String username, String password, String role) {
        return new UserRequest(username, password, role);
    }
}

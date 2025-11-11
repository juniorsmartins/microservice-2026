package backend.finance.application.dtos.request;

public record UserRequest(

        String username,

        String password,

        String role
) {
}

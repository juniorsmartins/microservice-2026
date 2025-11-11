package backend.finance.application.dtos.request;

public record CustomerRequest(

        String name,

        String email,

        UserRequest user
) {
}

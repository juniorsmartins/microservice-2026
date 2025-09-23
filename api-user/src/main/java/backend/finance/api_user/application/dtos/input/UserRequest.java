package backend.finance.api_user.application.dtos.input;

import jakarta.validation.constraints.NotBlank;

public record UserRequest(

        @NotBlank
        String username,

        @NotBlank
        String password,

        @NotBlank
        String role
) {
}

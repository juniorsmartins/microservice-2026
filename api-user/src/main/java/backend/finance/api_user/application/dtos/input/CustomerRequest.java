package backend.finance.api_user.application.dtos.input;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CustomerRequest(

        @NotBlank
        String name,

        @Email
        String email,

        @NotNull
        UserRequest user
) {
}

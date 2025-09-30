package backend.finance.api_user.application.dtos.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import static backend.finance.api_user.domain.constant.ConstantsValidation.PASSWORD_SIZE_MAX;
import static backend.finance.api_user.domain.constant.ConstantsValidation.USERNAME_SIZE_MAX;

public record UserRequest(

        @NotBlank
        @Size(max = USERNAME_SIZE_MAX)
        String username,

        @NotBlank
        @Size(max = PASSWORD_SIZE_MAX)
        String password,

        @NotBlank
        String role
) {
}

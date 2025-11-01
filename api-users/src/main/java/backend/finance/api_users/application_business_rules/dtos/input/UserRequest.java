package backend.finance.api_users.application_business_rules.dtos.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import static backend.finance.api_users.enterprise_business_rules.constants.ConstantsValidation.PASSWORD_SIZE_MAX;
import static backend.finance.api_users.enterprise_business_rules.constants.ConstantsValidation.USERNAME_SIZE_MAX;

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

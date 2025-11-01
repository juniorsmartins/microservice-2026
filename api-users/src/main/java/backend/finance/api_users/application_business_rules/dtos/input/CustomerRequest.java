package backend.finance.api_users.application_business_rules.dtos.input;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import static backend.finance.api_users.enterprise_business_rules.constants.ConstantsValidation.NAME_SIZE_MAX;

public record CustomerRequest(

        @NotBlank
        @Size(max = NAME_SIZE_MAX)
        String name,

        @Email
        String email,

        @NotNull
        @Valid
        UserRequest user
) {
}

package backend.finance.api_user.application.dtos.input;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import static backend.finance.api_user.domain.constant.ConstantsValidation.EMAIL_SIZE_MAX;
import static backend.finance.api_user.domain.constant.ConstantsValidation.NAME_SIZE_MAX;

public record CustomerRequest(

        @NotBlank
        @Size(max = NAME_SIZE_MAX)
        String name,

        @Email
        @Size(max = EMAIL_SIZE_MAX)
        String email,

        @NotNull
        @Valid
        UserRequest user
) {
}

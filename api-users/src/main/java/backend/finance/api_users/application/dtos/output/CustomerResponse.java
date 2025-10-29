package backend.finance.api_users.application.dtos.output;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.UUID;

@JsonPropertyOrder({"id", "nome", "email", "user"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CustomerResponse(

        UUID id,

        String name,

        String email,

        UserResponse user,

        boolean active
) {
}

package backend.finance.api_users.application.dtos.output;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.UUID;

@JsonPropertyOrder({"id", "username"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserResponse(

        UUID id,

        String username,

        boolean active
) {
}

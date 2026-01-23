package backend.ia.infrastructure.dtos.request;

import jakarta.validation.constraints.NotBlank;

public record ChatRequest(@NotBlank String prompt) {
}

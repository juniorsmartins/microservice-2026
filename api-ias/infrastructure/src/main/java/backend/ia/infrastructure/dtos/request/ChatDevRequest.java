package backend.ia.infrastructure.dtos.request;

import jakarta.validation.constraints.NotBlank;

public record ChatDevRequest(

        @NotBlank
        String javaVersion,

        @NotBlank
        String springBootVersion,

        @NotBlank
        String gradleVersion,

        @NotBlank
        String springCloudVersion,

        @NotBlank
        String apisRestList,

        @NotBlank
        String questao

) {
}

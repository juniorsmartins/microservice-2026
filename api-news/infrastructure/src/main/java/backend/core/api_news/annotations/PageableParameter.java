package backend.core.api_news.annotations;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = {ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Parameter(
        in = ParameterIn.QUERY, name = "page", description = "Número da página inicial (0..N)",
        schema = @Schema(type = "integer", defaultValue = "0")
)
@Parameter(
        in = ParameterIn.QUERY, name = "size", description = "Quantidade de elementos por página.",
        schema = @Schema(type = "integer", defaultValue = "5")
)
@Parameter(
        in = ParameterIn.QUERY, name = "sort", description = "Critério de ordenação de página. Exemplos: createdDate,desc ou lastModifiedDate,asc."
)
public @interface PageableParameter {
}

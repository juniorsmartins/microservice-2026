package backend.core.api_news.dtos.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "NewsCreateRequest", description = "Objeto para transporte de dados de entrada em requisições.")
public record NewsRequest(

        @Schema(name = "Chapéu", description = "Elemento editorial usado acima do título da matéria.", example = "Esporte")
        @NotBlank
        String hat,

        @Schema(name = "Título", description = "Frase principal responsável por atrair e apresentar a matéria.", example = "Arsenal consagra-se campeão ao vencer Coritiba")
        @NotBlank
        String title,

        @Schema(name = "Linha fina", description = "Frase complementar ao título, posicionada logo abaixo e mais aprofundada.", example = "O Quarterback do Arsenal, Kudiba marcou 60% dos pontos com pases para o WideReceiver, Tommy.")
        @NotBlank
        String thinLine,

        @Schema(name = "Texto", description = "O corpo da matéria onde a história é contada.", example = "Testo longo dissertando sobre o tema proposto no título.")
        @NotBlank
        String text,

        @Schema(name = "Autor", description = "O nome do responsável por escrever a matéria.", example = "Joseph Pulitzer")
        @NotBlank
        String author,

        @Schema(name = "Fonte", description = "Pessoa, instituição, documento ou dado que confirma os fatos e é a origem da informação.", example = "IBGE")
        @NotBlank
        String font

) {
}

package backend.core.api_news.dtos.responses;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(name = "NewsResponse", description = "Objeto para transporte de dados de saída em requisições.")
public record NewsResponse(

        @Schema(name = "ID", description = "Identificador único do recurso.", example = "92e719aa-3c45-4387-95a2-5d078bf410ed")
        UUID id,

        @Schema(name = "chapéu", description = "Elemento editorial usado acima do título da matéria.", example = "Esporte")
        String hat,

        @Schema(name = "título", description = "Frase principal responsável por atrair e apresentar a matéria.", example = "Arsenal consagra-se campeão ao vencer Coritiba")
        String title,

        @Schema(name = "Linha fina", description = "Frase complementar ao título, posicionada logo abaixo e mais aprofundada.", example = "O Quarterback do Arsenal, Kudiba marcou 60% dos pontos com pases para o WideReceiver, Tommy.")
        String thinLine,

        @Schema(name = "texto", description = "O corpo da matéria onde a história é contada.")
        String text,

        @Schema(name = "fonte", description = "Pessoa, instituição, documento ou dado que confirma os fatos e é a origem da informação.", example = "IBGE")
        String font
) implements java.io.Serializable {
}

package backend.core.api_news.dtos.requests;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "NewsCreateRequest", description = "Objeto para transporte de dados de entrada em requisições.")
public record NewsCreateRequest(

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
) {
}

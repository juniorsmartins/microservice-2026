package backend.core.api_news.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ApiVersionConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ApiVersionConfig implements WebMvcConfigurer {

    @Override
    public void configureApiVersioning(ApiVersionConfigurer configurer) {
        configurer
//                .usePathSegment(1) // Adicionar um resolvedor para extrair a versão de um segmento de caminho.
                .useMediaTypeParameter(MediaType.APPLICATION_JSON, "version")
                .addSupportedVersions("1.0", "2.0") // Adicionar à lista de versões suportadas para verificação antes de gerar um erro InvalidApiVersionExceptionpara versões desconhecidas.
                .setDefaultVersion("1.0") // Configure uma versão padrão para atribuir às solicitações que não especificarem uma.
                .setVersionRequired(false); // Permite ou não caminhos sem versão
    }
}

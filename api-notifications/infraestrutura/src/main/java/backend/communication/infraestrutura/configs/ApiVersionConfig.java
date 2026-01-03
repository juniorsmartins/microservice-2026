package backend.communication.infraestrutura.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ApiVersionConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ApiVersionConfig implements WebMvcConfigurer {

    @Override
    public void configureApiVersioning(ApiVersionConfigurer configurer) {
        configurer
                .usePathSegment(1)
                .useMediaTypeParameter(MediaType.APPLICATION_JSON, "version")
                .addSupportedVersions("1.0", "2.0", "3.0")
                .setDefaultVersion("1.0");
    }
}

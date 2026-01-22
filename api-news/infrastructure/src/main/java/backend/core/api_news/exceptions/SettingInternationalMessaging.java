package backend.core.api_news.exceptions;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.Locale;

@Configuration
public class SettingInternationalMessaging implements WebMvcConfigurer {

    @Bean // Configura a fonte de mensagens internacionalizadas
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:exceptions/messages"); // Define a localização dos arquivos de mensagens
        messageSource.setDefaultEncoding("UTF-8"); // Garante suporte a caracteres acentuados
        return messageSource;
    }

    @Bean // Determina qual idioma/locale usar para cada requisição
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver(); // Define como receber o idioma do usuário (pode armazenar em Cookie, Sessão ou via requisição - Accept-Language)
        localeResolver.setDefaultLocale(new Locale.Builder() // Define o padrão como pt_BR
                .setLanguage("pt")
                .setRegion("BR")
                .build()
        );
        return localeResolver;
    }

    @Bean // Responsável por interceptar as requisições e verificar se há alguma mudança de idioma.
    public LocaleChangeInterceptor localeChangeInterceptor() {
        return new LocaleChangeInterceptor();
    }

    @Override // Registra o interceptor no Spring MVC. Executado antes de cada requisição para verificar/setar o locale
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor()); // Registra o localeChangeInterceptor para que ele possa interceptar as requisições
    }
}

package backend.finance.api_user.application.configs.exception;

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

    // Define a localização dos arquivos de mensagens
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:exceptions/messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    // Define como receber o idioma do usuário (pode armazenar em Cookie, Sessão ou via requisição - Accept-Language)
    // Também define a localização padrão
    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
        localeResolver.setDefaultLocale(new Locale.Builder().setLanguage("pt").setRegion("BR").build());
        return localeResolver;
    }

    // Responsável por interceptar as requisições e verificar se há alguma mudança de idioma.
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        return new LocaleChangeInterceptor();
    }

    // Registra o localeChangeInterceptor para que ele possa interceptar as requisições
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
}

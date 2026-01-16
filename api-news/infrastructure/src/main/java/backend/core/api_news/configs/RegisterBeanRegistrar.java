package backend.core.api_news.configs;

import backend.core.api_news.usecases.NewsCreateUseCase;
import backend.core.api_news.usecases.NewsDeleteByIdUseCase;
import org.springframework.beans.factory.BeanRegistrar;
import org.springframework.beans.factory.BeanRegistry;
import org.springframework.core.env.Environment;

public class RegisterBeanRegistrar implements BeanRegistrar {

    @Override
    public void register(BeanRegistry registry, Environment env) {

        registry.registerBean("newsCreateUseCase", NewsCreateUseCase.class,
                spec -> spec.description("Serviço de criar News."));

        registry.registerBean("newsDeleteByIdUseCase", NewsDeleteByIdUseCase.class,
                spec -> spec.description("Serviço de deletar News por Id."));
    }
}

# TUTORIAL

## 1. Teoria

### Fontes:
- https://docs.spring.io/spring-ai/reference/2.0/api/chatclient.html 
- https://docs.spring.io/spring-ai/reference/2.0/api/chatmodel.html 
- https://docs.spring.io/spring-ai/reference/2.0/api/multimodality.html 
- https://docs.spring.io/spring-ai/reference/2.0/api/chat/google-genai-chat.html 
- https://ai.google.dev/gemini-api/docs?hl=pt-br#java 
- https://docs.spring.io/spring-ai/reference/2.0/api/chat/deepseek-chat.html 
- 
- 
- https://www.youtube.com/watch?v=daPwd4DnEfA 
- https://spring.io/blog/2025/12/11/spring-ai-2-0-0-M1-available-now 
- https://github.com/spring-projects/spring-ai 
- https://github.com/spring-ai-community/awesome-spring-ai 
- https://platform.openai.com/docs/quickstart?language=java 
- https://platform.openai.com/settings/organization/billing/overview 
- 
-
- https://docs.spring.io/spring-ai/reference/getting-started.html
- https://spring.io/blog/2024/11/19/why-spring-ai
- 
-
- https://www.baeldung.com/spring-ai 
- https://www.youtube.com/watch?v=FzLABAppJfM 
- https://www.youtube.com/watch?v=NscHAlj-yQ0 
- 
- https://usama.codes/blog/spring-ai-2-spring-boot-4-guide
- https://www.youtube.com/playlist?list=PLZV0a2jwt22uoDm3LNDFvN6i2cAVU_HTH 
- 
- 
- https://www.youtube.com/watch?v=oP7tn_YfoOk&list=PLuNxlOYbv61hmSWcdM0rtoWT0qEjZMIhU&index=1 (playlist)
- 


### Introdução: 

```
O Spring AI fornece camada de abstração comum para trabalhar com diferentes provedores de IA usando os 
padrões de programação Spring já conhecidos. Isso elimina a necessidade de usar explicitamente SDKs 
específicos do provedor e nos permite alternar entre diferentes modelos sem alterar o código do 
aplicativo.

Pré-requisitos do Spring AI 2.0.0-M1: 
- Java 21;
- Spring Boot 4.0; 
- Spring Framework 7.0.




```


## 2. Configuração


### Passo-a-passo

Pré-requisitos (OpenAI):
1. Criar APi Key do OpenAI;
   a. https://platform.openai.com/settings/organization/api-keys
2. Inserir crédito (não é grátis).

Cliente (OpenAI):
1. Adicionar dependência;
    a. Spring AI Model OpenAI (spring-ai-starter-model-openai).
2. Adicionar configuração no application.yml;
    a. Configuração de IA;
    b. Configuração de Logging.
3. Criar ChatController e seus DTOs;
4. Faça Post no endpoint do chat para testar.


Pré-requisitos (OpenAI + Gemini AI):
1. Criar API Key do OpenAI (https://platform.openai.com/settings/organization/api-keys);
2. Criar API Key do Gemini (https://aistudio.google.com/app/api-keys).

Cliente com múltiplos modelos de IA (OpenAI + Gemini AI):
1. Adicionar dependência;
   a. Spring AI Model OpenAI (spring-ai-starter-model-openai);
   b. Spring AI Model Google GenAI (spring-ai-starter-model-google-genai).
2. Adicionar configuração no application.yml;
   a. Desativação de client padrão para usar múltiplos modelos;
   b. Configuração de OpenAI;
   c. Configuração de Google GenAI;
   d. Configuração de Logging.
3. Criar classe de configuração dos beans dos dois modelos;
4. Criar ChatController e DTOs;
5. Configuração no docker compose (chaves de IAs estão no arquivo env para esconder segredos);
6. Faça Post no endpoint do chat para testar.


Pré-requisitos (OpenAI + Gemini AI + Deepseek):
1. Criar API Key do OpenAI (https://platform.openai.com/settings/organization/api-keys);
2. Criar API Key do Gemini (https://aistudio.google.com/app/api-keys);
3. Criar API Key do Deepseek (https://platform.deepseek.com/api_keys).

Cliente com múltiplos modelos de IA (OpenAI + Gemini AI + Deepseek):
1. Adicionar dependência;
   a. Spring AI Model OpenAI (spring-ai-starter-model-openai);
   b. Spring AI Model Google GenAI (spring-ai-starter-model-google-genai);
   c. Spring AI Model Deepseek (spring-ai-starter-model-deepseek).
2. Adicionar configuração no application.yml;
   a. Desativação de client padrão para usar múltiplos modelos;
   b. Configuração de OpenAI;
   c. Configuração de Google GenAI;
   d. Configuração de Deepseek;
   e. Configuração de Logging.
3. Criar classe de configuração dos beans dos três modelos;
4. Criar ChatController e DTOs;
5. Configuração no docker compose (chaves de IAs estão no arquivo env para esconder segredos);



Pré-requisitos (OpenRouter AI):
1. Criar APi Key do Deepseek;
   a. https://openrouter.ai/settings/keys
2. Grátis com limitações.

Cliente (OpenRouter AI):
1. Adicionar dependência;
   a. Spring AI Model OpenAI (spring-ai-starter-model-openai).
2. Adicionar configuração no application.yml;
   a. Configuração de IA;
   b. Configuração de Logging;
3. Criar ChatController e seus DTOs;
4. Faça Post no endpoint do chat para testar.





### Implementação: 

Cliente (OpenAI):

1. Adicionar dependência;
   a. Spring AI Model OpenAI (spring-ai-starter-model-openai).
```
ext {
    set('springAiVersion', "2.0.0-M2")
}

dependencies {

    implementation 'org.springframework.ai:spring-ai-starter-model-openai'
}

dependencyManagement {
    imports {
        mavenBom "org.springframework.ai:spring-ai-bom:${springAiVersion}"
    }
}
```

2. Adicionar configuração no application.yml;
   a. Configuração de IA;
   b. Configuração de Logging;
```
spring:
  ai:
    openai:
      base-url: ${OPENAI_BASE_URL:https://api.openai.com}
      api-key: ${OPENAI_API_KEY}
      chat:
        options:
          model: gpt-5-nano
          temperature: 1

logging:
  level:
    org.springframework.ai: INFO
    org.springframework.ai.chat.client.advisor: DEBUG 
```

3. Criar ChatController e seus DTOs;
```
@Tag(name = "Chat", description = "Controlador do recurso de Chat de Ias.")
@Slf4j
@NullMarked
@RestController
@RequestMapping(path = {"/api/"})
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build(); 
    }

    @PostMapping(value = "/{version}/ias/openai/chat", version = "1.0")
    public ChatResponse chat(@RequestBody @Valid ChatRequest input) {
        var response = chatClient.prompt(input.prompt()).call().content();
        return new ChatResponse(response);
    }
}

public record ChatRequest(@NotBlank String prompt) {
}

public record ChatResponse(String prompt) {
}
```


Cliente com múltiplos modelos de IA (OpenAI + Gemini AI):

1. Adicionar dependência;
   a. Spring AI Model OpenAI (spring-ai-starter-model-openai);
   b. Spring AI Model Google GenAI (spring-ai-starter-model-google-genai).
```
ext {
    set('springAiVersion', "2.0.0-M2")
}

dependencies {

    implementation 'org.springframework.ai:spring-ai-starter-model-openai'
    implementation 'org.springframework.ai:spring-ai-starter-model-google-genai'
}

dependencyManagement {
    imports {
        mavenBom "org.springframework.ai:spring-ai-bom:${springAiVersion}"
    }
}
```

2. Adicionar configuração no application.yml;
   a. Desativação de client padrão para usar múltiplos modelos;
   b. Configuração de OpenAI;
   c. Configuração de Google GenAI;
   d. Configuração de Logging.
```
spring:
  ai:
    chat:
      client:
        enabled: false # Desativa o cliente padrão para usar múltiplos modelos

    openai:
      base-url: ${OPENAI_BASE_URL:https://api.openai.com}
      api-key: ${OPENAI_API_KEY}
      chat:
        options:
          model: gpt-5-nano
          temperature: 1

    google:
      genai:
        api-key: ${GEMINI_API_KEY}
        chat:
          options:
            model: gemini-2.0-flash
            temperature: 0.1
```

3. Criar classe de configuração dos beans dos dois modelos;
```
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatAiClientConfig {

    @Bean(name = "openAiChatClient")
    public ChatClient openAiChatClient(OpenAiChatModel openAiChatModel) {
        return ChatClient.builder(openAiChatModel)
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }

    @Bean(name = "geminiAiChatClient")
    public ChatClient geminiAiChatClient(GoogleGenAiChatModel googleGenAiChatModel) {
        return ChatClient.builder(googleGenAiChatModel)
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }
}
```

4. Criar ChatController e DTOs;
```
import backend.ia.infrastructure.dtos.request.ChatRequest;
import backend.ia.infrastructure.dtos.response.ChatResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Chat", description = "Controlador do recurso de Chat de Ias.")
@Slf4j
@NullMarked
@RestController
@RequestMapping(path = {"/api/"})
public class ChatController {

    private final ChatClient openAiChatClient;

    private final ChatClient geminiAiChatClient;

    public ChatController(
            @Qualifier("openAiChatClient") ChatClient openAiChatClient,
            @Qualifier("geminiAiChatClient") ChatClient geminiAiChatClient) {
        this.openAiChatClient = openAiChatClient;
        this.geminiAiChatClient = geminiAiChatClient;
    }

    @PostMapping(value = "/{version}/ias/openai/chat", version = "1.0")
    public ChatResponse chatOpenAi(@RequestBody @Valid ChatRequest input) {
        var response = openAiChatClient.prompt(input.prompt()).call().content();
        return new ChatResponse(response);
    }

    @PostMapping(value = "/{version}/ias/gemini/chat", version = "1.0")
    public ChatResponse chatGemini(@RequestBody @Valid ChatRequest input) {
        var response = geminiAiChatClient.prompt(input.prompt()).call().content();
        return new ChatResponse(response);
    }
}

public record ChatRequest(@NotBlank String prompt) {
}

public record ChatResponse(String prompt) {
}
```

5. Configuração no docker compose (chaves de IAs estão no arquivo .env para esconder segredos - envs/.env-api-ias);
```
  api-ias:
    image: juniorsmartins/api-ias:v0.0.2
    container_name: api-ias
    hostname: api-ias
    build:
      context: ../api-ias
      dockerfile: Dockerfile
      args:
        APP_NAME: "api-ias"
        APP_VERSION: "v0.0.2"
        APP_DESCRIPTION: "Microsserviço responsável por fornecer inteligência artificial."
    env_file:
      - envs/.env-api-ias
    ports:
      - "9010:9010"
    deploy:
      resources:
        limits:
          cpus: '0.5'
          memory: 512M
        reservations:
          memory: 256M
          cpus: '0.3'
    environment:
      TZ: utc
      SERVER_PORT: 9010
      SPRING_CLOUD_CONFIG_SERVER_URI: http://configserver:8888
      EUREKA_CLIENT_SERVICEURL_DEFAULTZONE: http://eurekaserver:8761/eureka/
      SPRING_PROFILES_ACTIVE: dev
      SPRING_RABBITMQ_HOST: "rabbit"
      RABBIT_HOST: rabbit
      RABBIT_PORT: 5672
      REDIS_HOST: redis
      REDIS_PORT: 6379
      OPENAI_BASE_URL: https://api.openai.com
      JAVA_TOOL_OPTIONS: "--enable-native-access=ALL-UNNAMED" # Elimina alguns warnnings
    restart: unless-stopped
    networks:
      - communication
    depends_on:
      schema-registry:
        condition: service_healthy
      configserver:
        condition: service_healthy
      eurekaserver:
        condition: service_healthy
      redis:
        condition: service_healthy
```

6. Faça Post no endpoint do chat para testar.
```
Final dos endpoints: 

/api/1.0/ias/openai/chat
/api/1.0/ias/gemini/chat

Corpo da mensagem: 

{
    "prompt": "Qual o resultado dessa equação: 2 + 2"
}
```


Cliente com múltiplos modelos de IA (OpenAI + Gemini AI + Deepseek):

1. Adicionar dependência;
   a. Spring AI Model OpenAI (spring-ai-starter-model-openai);
   b. Spring AI Model Google GenAI (spring-ai-starter-model-google-genai);
   c. Spring AI Model Deepseek (spring-ai-starter-model-deepseek).
```
ext {
    set('springAiVersion', "2.0.0-M2")
}

dependencies {

    implementation 'org.springframework.ai:spring-ai-starter-model-openai'
    implementation 'org.springframework.ai:spring-ai-starter-model-google-genai'
    implementation 'org.springframework.ai:spring-ai-starter-model-deepseek'
}

dependencyManagement {
    imports {
        mavenBom "org.springframework.ai:spring-ai-bom:${springAiVersion}"
    }
}
```

2. Adicionar configuração no application.yml;
   a. Desativação de client padrão para usar múltiplos modelos;
   b. Configuração de OpenAI;
   c. Configuração de Google GenAI;
   d. Configuração de Deepseek;
   e. Configuração de Logging.
```
spring:
  ai:
    chat:
      client:
        enabled: false # Desativa o cliente padrão para usar múltiplos modelos

    openai:
      base-url: ${OPENAI_BASE_URL}
      api-key: ${OPENAI_API_KEY}
      chat:
        options:
          model: gpt-5-nano
          temperature: 1

    google:
      genai:
        api-key: ${GEMINI_API_KEY}
        chat:
          options:
            model: gemini-2.0-flash
            temperature: 0.1

    deepseek:
      base-url: ${DEEPSEEK_BASE_URL}
      api-key: ${DEEPSEEK_API_KEY}
      chat:
        options:
          model: deepseek-chat
          temperature: 2
```

3. Criar classe de configuração dos beans dos três modelos;
```
@Configuration
public class ChatAiClientConfig {

    @Bean(name = "openAiChatClient")
    public ChatClient openAiChatClient(OpenAiChatModel openAiChatModel) {
        return ChatClient.builder(openAiChatModel)
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }

    @Bean(name = "geminiAiChatClient")
    public ChatClient geminiAiChatClient(GoogleGenAiChatModel googleGenAiChatModel) {
        return ChatClient.builder(googleGenAiChatModel)
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }

    @Bean(name = "deepseekAiChatClient")
    public ChatClient deepseekAiChatClient(DeepSeekChatModel deepSeekChatModel) {
        return ChatClient.builder(deepSeekChatModel)
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }
}
```

4. Criar ChatController e DTOs;
```
@Tag(name = "Chat", description = "Controlador do recurso de Chat de Ias.")
@Slf4j
@NullMarked
@RestController
@RequestMapping(path = {"/api/"})
public class ChatController {

    private final ChatClient openAiChatClient;

    private final ChatClient geminiAiChatClient;

    private final ChatClient deepseekAiChatClient;

    public ChatController(
            @Qualifier("openAiChatClient") ChatClient openAiChatClient,
            @Qualifier("geminiAiChatClient") ChatClient geminiAiChatClient,
            @Qualifier("deepseekAiChatClient") ChatClient deepseekAiChatClient) {
        this.openAiChatClient = openAiChatClient;
        this.geminiAiChatClient = geminiAiChatClient;
        this.deepseekAiChatClient = deepseekAiChatClient;
    }

    @PostMapping(value = "/{version}/ias/openai/chat", version = "1.0")
    public ChatResponse chatOpenAi(@RequestBody @Valid ChatRequest input) {
        var response = openAiChatClient.prompt(input.prompt()).call().content();
        return new ChatResponse(response);
    }

    @PostMapping(value = "/{version}/ias/gemini/chat", version = "1.0")
    public ChatResponse chatGemini(@RequestBody @Valid ChatRequest input) {
        var response = geminiAiChatClient.prompt(input.prompt()).call().content();
        return new ChatResponse(response);
    }

    @PostMapping(value = "/{version}/ias/deepseek/chat", version = "1.0")
    public ChatResponse chatDeepseek(@RequestBody @Valid ChatRequest input) {
        var response = deepseekAiChatClient.prompt(input.prompt()).call().content();
        return new ChatResponse(response);
    }
}
```

5. Configuração no docker compose (chaves de IAs estão no arquivo env para esconder segredos);
```
  api-ias:
    image: juniorsmartins/api-ias:v0.0.2
    container_name: api-ias
    hostname: api-ias
    build:
      context: ../api-ias
      dockerfile: Dockerfile
      args:
        APP_NAME: "api-ias"
        APP_VERSION: "v0.0.2"
        APP_DESCRIPTION: "Microsserviço responsável por fornecer inteligência artificial."
    env_file:
      - envs/.env-api-ias
    ports:
      - "9010:9010"
    deploy:
      resources:
        limits:
          cpus: '0.5'
          memory: 512M
        reservations:
          memory: 256M
          cpus: '0.3'
    environment:
      TZ: utc
      SERVER_PORT: 9010
      SPRING_CLOUD_CONFIG_SERVER_URI: http://configserver:8888
      EUREKA_CLIENT_SERVICEURL_DEFAULTZONE: http://eurekaserver:8761/eureka/
      SPRING_PROFILES_ACTIVE: dev
      SPRING_RABBITMQ_HOST: "rabbit"
      RABBIT_HOST: rabbit
      RABBIT_PORT: 5672
      REDIS_HOST: redis
      REDIS_PORT: 6379
      OPENAI_BASE_URL: https://api.openai.com
      DEEPSEEK_BASE_URL: https://api.deepseek.com
      JAVA_TOOL_OPTIONS: "--enable-native-access=ALL-UNNAMED" # Elimina alguns warnnings
    restart: unless-stopped
    networks:
      - communication
    depends_on:
      schema-registry:
        condition: service_healthy
      configserver:
        condition: service_healthy
      eurekaserver:
        condition: service_healthy
      redis:
        condition: service_healthy
```

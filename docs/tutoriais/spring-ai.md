# TUTORIAL

## 1. Teoria

### Fontes:
- https://docs.spring.io/spring-ai/reference/2.0/api/chatclient.html 
- https://docs.spring.io/spring-ai/reference/2.0/api/chatmodel.html 
- https://docs.spring.io/spring-ai/reference/2.0/api/multimodality.html 
- https://docs.spring.io/spring-ai/reference/2.0/api/chat/google-genai-chat.html 
- https://ai.google.dev/gemini-api/docs?hl=pt-br#java 
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
   a. Spring AI Model OpenAI (spring-ai-starter-model-openai).
2. Adicionar configuração no application.yml;
   a. Configuração de IA;
   b. Configuração de Logging;
3. Criar ChatController e seus DTOs;
4. Faça Post no endpoint do chat para testar.


Pré-requisitos (Deepseek AI):
1. Criar APi Key do Deepseek;
   a. https://platform.deepseek.com/api_keys
2. Grátis com limitações.

Cliente (Deepseek AI):
1. Adicionar dependência;
   a. Spring AI Model OpenAI (spring-ai-starter-model-openai).
2. Adicionar configuração no application.yml;
   a. Configuração de IA;
   b. Configuração de Logging;
3. Criar ChatController e seus DTOs;
4. Faça Post no endpoint do chat para testar.


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




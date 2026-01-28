package backend.ia.infrastructure.controllers;

import backend.ia.infrastructure.dtos.request.ChatDevRequest;
import backend.ia.infrastructure.dtos.request.ChatRequest;
import backend.ia.infrastructure.dtos.response.ChatResponse;
import backend.ia.infrastructure.dtos.response.TweetResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Tag(name = "Chat", description = "Controlador do recurso de Chat de Ias.")
@Slf4j
@NullMarked
@RestController
@RequestMapping(path = {"/api/"})
public class ChatController {

    private final ChatClient openAiChatClient;

    private final ChatClient geminiAiChatClient;

    private final ChatClient deepseekAiChatClient;

    private final ChatClient anthropicAiChatClient;

    private final ChatClient ollamaAiChatClient;

    public ChatController(
            @Qualifier("openAiChatClient") ChatClient openAiChatClient,
            @Qualifier("geminiAiChatClient") ChatClient geminiAiChatClient,
            @Qualifier("deepseekAiChatClient") ChatClient deepseekAiChatClient,
            @Qualifier("anthropicAiChatClient") ChatClient anthropicAiChatClient,
            @Qualifier("ollamaAiChatClient") ChatClient ollamaAiChatClient) {
        this.openAiChatClient = openAiChatClient;
        this.geminiAiChatClient = geminiAiChatClient;
        this.deepseekAiChatClient = deepseekAiChatClient;
        this.anthropicAiChatClient = anthropicAiChatClient;
        this.ollamaAiChatClient = ollamaAiChatClient;
    }

    @PostMapping(value = "/{version}/ias/openai/chat", version = "1.0")
    public ChatResponse chatOpenAi(@RequestBody @Valid ChatRequest input) {

        String systemPrompt = "Você é um assistente profissional e engraçado. Sempre responde de forma objetiva, explicativa, detalhada e bem humorada.";
        SystemMessage systemMessage = new SystemMessage(systemPrompt);
        UserMessage userMessage = new UserMessage(input.prompt());

        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));
        var response = openAiChatClient.prompt(prompt).call().content();

        return new ChatResponse(response);
    }

    @PostMapping(value = "/{version}/ias/openai/chat/dev-java", version = "1.0")
    public ChatResponse chatOpenAi(@RequestBody @Valid ChatDevRequest input) {

        PromptTemplate promptTemplate = new PromptTemplate("""
                Tenho um microsserviços escrito em Java (versão {javaVersion}), Spring Boot (versão {springBootVersion}), 
                Gradle (versão {gradleVersion}) e Spring Cloud (versão {springCloudVersion}).
                
                Esse microsserviços possui ConfigServer, EurekaServer, GatewayServer, Cache com Redis e já possui 
                as seguintes APIs Rest: {apisRestList}. 
                
                Esse microsserviços possui o objetivo de fornecer diversas ferramentas para Jornalistas e Assessores de Imprensa.
                
                Agora me ajude com a seguinte questão: {questao}. 
                
                Para que eu possa entender melhor, sempre explique os detalhes e exemplifique com código.
                """);

        Map<String, Object> vars = Map.of(
                "javaVersion", input.javaVersion(), "springBootVersion", input.springBootVersion(),
                "gradleVersion", input.gradleVersion(), "springCloudVersion", input.springCloudVersion(),
                "apisRestList", input.apisRestList(), "questao", input.questao());

        Message message = promptTemplate.createMessage(vars);
        var response = openAiChatClient.prompt().messages(message).call().content();

        return new ChatResponse(response);
    }

    @PostMapping(value = "/{version}/ias/openai/chat/tweet", version = "1.0")
    public TweetResponse chatOpenAiTweet(@RequestBody @Valid ChatRequest input) {

        SystemMessage systemMessage = new SystemMessage(StandardCharsets.UTF_8.toString());

        PromptTemplate promptTemplate = new PromptTemplate("""
                Gere um tweet para o seguinte conteúdo: 
                
                {content}
                
                {format}
                """);

        BeanOutputConverter<TweetResponse> beanOutputConverter = new BeanOutputConverter<>(TweetResponse.class);
        String format = beanOutputConverter.getFormat();
        Map<String, Object> vars = Map.of("content", input.prompt(), "format", format);

        Message userMessage = promptTemplate.createMessage(vars);

        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));
        String response = openAiChatClient.prompt(prompt).call().content();

        return beanOutputConverter.convert(response);
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

    @PostMapping(value = "/{version}/ias/anthropic/chat", version = "1.0")
    public ChatResponse chatAnthropic(@RequestBody @Valid ChatRequest input) {
        var response = anthropicAiChatClient.prompt(input.prompt()).call().content();
        return new ChatResponse(response);
    }

    @PostMapping(value = "/{version}/ias/ollama/chat", version = "1.0")
    public ResponseEntity<ChatResponse> chatOllama(
            @RequestBody @Valid ChatRequest input,
            @CookieValue(name = "X-CONV-ID", required = false) String convId) {

        var conversationId = convId == null ? UUID.randomUUID().toString() : convId;
        var responseCookie = createResponseCookie("X-CONV-ID", conversationId, 3600);

        var response = ollamaAiChatClient.prompt()
                .user(input.prompt())
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, conversationId) )
                .call()
                .content();
        var chatResponse = new ChatResponse(response);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(chatResponse);
    }

    private ResponseCookie createResponseCookie(String name, String conversationId, int maxAgeSeconds) {

        return ResponseCookie.from(name, conversationId)
                .path("/")
                .maxAge(maxAgeSeconds)
                .build();
    }
}

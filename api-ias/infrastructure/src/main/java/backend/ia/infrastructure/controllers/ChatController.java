package backend.ia.infrastructure.controllers;

import backend.ia.infrastructure.dtos.request.ChatRequest;
import backend.ia.infrastructure.dtos.response.ChatResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;

@Tag(name = "Chat", description = "Controlador do recurso de Chat de Ias.")
@Slf4j
@NullMarked
@RestController
@RequestMapping(path = {"/api/"})
public class ChatController {

    private final ChatClient openAiChatClient;

    private final ChatClient geminiAiChatClient;

    public ChatController(OpenAiChatModel openAiChatClient, GoogleGenAiChatModel geminiAiChatClient) {

        this.openAiChatClient = ChatClient.builder(openAiChatClient)
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();

        this.geminiAiChatClient = ChatClient.builder(geminiAiChatClient)
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
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

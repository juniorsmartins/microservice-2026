package backend.ia.infrastructure.controllers;

import backend.ia.infrastructure.dtos.request.ChatRequest;
import backend.ia.infrastructure.dtos.response.ChatResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
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

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build(); // Aqui podem ser feitas personalizações
    }

    @PostMapping(value = "/{version}/ias/chat", version = "1.0")
    public ChatResponse chat(@RequestBody @Valid ChatRequest input) {
        var response = chatClient.prompt(input.prompt()).call().content();
        return new ChatResponse(response);
    }
}

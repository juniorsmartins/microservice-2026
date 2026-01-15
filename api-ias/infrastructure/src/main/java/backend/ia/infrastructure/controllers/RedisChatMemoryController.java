package backend.ia.infrastructure.controllers;

import backend.ia.infrastructure.dtos.ChatRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "RedisChatMemory", description = "Controlador do recurso de Chat de Ias.")
@Slf4j
@NullMarked
@RestController
@RequestMapping(path = {"/api/"})
public class RedisChatMemoryController {

//    private final ChatClient chatClient;
//
//    private final ChatMemoryRepository chatMemoryRepository;
//
//    public RedisChatMemoryController(OpenAiSdkChatModel openAiSdkChatModel, ChatMemoryRepository chatMemoryRepository) {
//        this.chatMemoryRepository = chatMemoryRepository;
//
//        ChatMemory chatMemory = MessageWindowChatMemory.builder()
//                .chatMemoryRepository(chatMemoryRepository)
//                .maxMessages(20)
//                .build();
//
//        this.chatClient = ChatClient.builder(openAiSdkChatModel)
//                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
//                .build();
//    }
//
//    /**
//     * Chat with persistent memory.
//     * Messages are stored in Redis and persist across sessions.
//     */
//    @PostMapping(value = "/{version}/ias/chat/{conversationId}", version = "1.0")
//    public Map<String, Object> chat(@PathVariable(name = "conversationId") String id, @RequestBody ChatRequest chatRequest) {
//
//        String response = chatClient.prompt()
//                .user(chatRequest.message())
//                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, id))
//                .call()
//                .content();
//
//        return Map.of("conversationId", id, "response", response);
//    }
//
//    /**
//     * Get conversation history for a specific conversation.
//     */
//    @GetMapping(value = "/{version}/ias/history/{conversationId}", version = "1.0")
//    public Map<String, Object> getHistory(@PathVariable(name = "conversationId") String id) {
//
//        var messages = chatMemoryRepository.findByConversationId(id);
//
//        List<Map<String, String>> history = messages.stream()
//                .map(msg -> Map.of(
//                        "type", msg.getMessageType().name(),
//                        "content", msg.getText()
//                ))
//                .toList();
//
//        return Map.of("conversationId", id, "messageCount", history.size(), "messages", history);
//    }
//
//    /**
//     * List all conversation IDs stored in Redis.
//     */
//    @GetMapping(value = "/{version}/ias/conversations", version = "1.0")
//    public Map<String, Object> listConversations() {
//
//        var conversationIds = chatMemoryRepository.findConversationIds();
//
//        return Map.of("count", conversationIds.size(), "conversationIds", conversationIds);
//    }
//
//    /**
//     * Delete a conversation from Redis.
//     */
//    @DeleteMapping(value = "/{version}/ias/history/{conversationId}", version = "1.0")
//    public Map<String, Object> deleteHistory(@PathVariable(name = "conversationId") String id) {
//        chatMemoryRepository.deleteByConversationId(id);
//
//        return Map.of(
//                "conversationId", id,
//                "deleted", true
//        );
//    }
}

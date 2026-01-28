package backend.ia.infrastructure.config;


import org.springframework.ai.anthropic.AnthropicChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.redis.RedisChatMemoryRepository;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPooled;

import java.time.Duration;

@Configuration
public class ChatAiClientConfig {

    @Bean(name = "openAiChatClient") // com memória padrão automática
    public ChatClient openAiChatClient(OpenAiChatModel openAiChatModel, ChatMemory chatMemory) {
        return ChatClient.builder(openAiChatModel)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build(), new SimpleLoggerAdvisor())
                .build();
    }

    @Bean(name = "geminiAiChatClient") // sem memória
    public ChatClient geminiAiChatClient(GoogleGenAiChatModel googleGenAiChatModel) {
        return ChatClient.builder(googleGenAiChatModel)
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }

    @Bean(name = "deepseekAiChatClient") // sem memória
    public ChatClient deepseekAiChatClient(DeepSeekChatModel deepSeekChatModel) {
        return ChatClient.builder(deepSeekChatModel)
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }

    @Bean(name = "anthropicAiChatClient") // com memória padrão automática
    public ChatClient anthropicAiChatClient(AnthropicChatModel anthropicChatModel, ChatMemory chatMemory) {
        return ChatClient.builder(anthropicChatModel)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build(), new SimpleLoggerAdvisor())
                .build();
    }

    @Bean(name = "ollamaAiChatClient") // Com memória Redis
    public ChatClient ollamaAiChatClient(OllamaChatModel ollamaChatModel) {

        var chatMemory = createRedisChatMemoryRepository();

        return ChatClient.builder(ollamaChatModel)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build(), new SimpleLoggerAdvisor())
                .build();
    }

    private ChatMemory createRedisChatMemoryRepository() {

        JedisPooled jedisPooled = new JedisPooled("redis", 6379);

        ChatMemoryRepository chatMemoryRepository = RedisChatMemoryRepository.builder()
                .jedisClient(jedisPooled)
                .indexName("api-ias-memory-chat-index")
                .keyPrefix("api-ias-memory-chat")
                .timeToLive(Duration.ofMinutes(10))
                .build();

        return createChatMemory(chatMemoryRepository, 25);
    }

    private ChatMemory createChatMemory(ChatMemoryRepository chatMemoryRepository, int maxMessages) {
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(chatMemoryRepository)
                .maxMessages(maxMessages)
                .build();
    }
}

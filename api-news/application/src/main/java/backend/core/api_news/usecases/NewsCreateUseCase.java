package backend.core.api_news.usecases;

import backend.core.api_news.dtos.NewsDto;
import backend.core.api_news.ports.input.NewsCreateInputPort;
import backend.core.api_news.ports.output.NewsSaveOutputPort;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Caching;

public class NewsCreateUseCase implements NewsCreateInputPort {

    private final NewsSaveOutputPort newsSaveOutputPort;

    public NewsCreateUseCase(NewsSaveOutputPort newsSaveOutputPort) {
        this.newsSaveOutputPort = newsSaveOutputPort;
    }

    @Caching(
            put = {
                    @CachePut(value = "newsById", key = "#result.id"),
                    @CachePut(value = "newsByTitle", key = "#result.title")
            },
            evict = {
                    @CacheEvict(value = "newsPage", allEntries = true) // Limpa cache de paginação
            }
    )
    @Override
    public NewsDto create(NewsDto dto) {
        return newsSaveOutputPort.save(dto);
    }
}

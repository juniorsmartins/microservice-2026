package backend.core.api_news.usecases;

import backend.core.api_news.dtos.NewsDto;
import backend.core.api_news.ports.input.NewsUpdateInputPort;
import backend.core.api_news.ports.output.NewsFindByIdOutputPort;
import backend.core.api_news.ports.output.NewsSaveOutputPort;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Caching;

public class NewsUpdateUseCase implements NewsUpdateInputPort {

    private final NewsFindByIdOutputPort newsFindByIdOutputPort;

    private final NewsSaveOutputPort newsSaveOutputPort;

    public NewsUpdateUseCase(NewsFindByIdOutputPort newsFindByIdOutputPort, NewsSaveOutputPort newsSaveOutputPort) {
        this.newsFindByIdOutputPort = newsFindByIdOutputPort;
        this.newsSaveOutputPort = newsSaveOutputPort;
    }

    @Caching(
            put = {
                    @CachePut(value = "newsById", key = "#result.id"),
            },
            evict = {
                    @CacheEvict(cacheNames = {"newsPageAll"}, allEntries = true)
            }
    )
    @Override
    public NewsDto update(NewsDto dto) {

        return newsFindByIdOutputPort.findById(dto.id())
                .map(dtoDatabase -> {
                    var dtoUpdated = this.toUpdate(dto, dtoDatabase);
                    return this.newsSaveOutputPort.save(dtoUpdated);
                })
                .orElseThrow(() -> new RuntimeException("News not found with id: " + dto.id()));
    }

    private NewsDto toUpdate(NewsDto dtoRequest, NewsDto dtoDatabase) {
        return new NewsDto(dtoDatabase.id(), dtoRequest.hat(), dtoRequest.title(), dtoRequest.thinLine(),
                dtoRequest.text(), dtoRequest.author(), dtoRequest.font(), dtoDatabase.createdBy(),
                dtoDatabase.lastModifiedBy(), dtoDatabase.createdDate(), dtoDatabase.lastModifiedDate());
    }
}

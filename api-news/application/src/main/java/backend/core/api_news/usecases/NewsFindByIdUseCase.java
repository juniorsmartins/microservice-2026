package backend.core.api_news.usecases;

import backend.core.api_news.dtos.NewsDto;
import backend.core.api_news.exceptions.http404.NewsNotFoundCustomException;
import backend.core.api_news.ports.input.NewsFindByIdInputPort;
import backend.core.api_news.ports.output.NewsFindByIdOutputPort;
import org.springframework.cache.annotation.Cacheable;

import java.util.UUID;

public class NewsFindByIdUseCase implements NewsFindByIdInputPort {

    private final NewsFindByIdOutputPort newsFindByIdOutputPort;

    public NewsFindByIdUseCase(NewsFindByIdOutputPort newsFindByIdOutputPort) {
        this.newsFindByIdOutputPort = newsFindByIdOutputPort;
    }

    @Cacheable(value = "newsById", key = "#id")
    @Override
    public NewsDto findById(final UUID id) {

        return newsFindByIdOutputPort.findById(id)
                .orElseThrow(() -> new NewsNotFoundCustomException(id));
    }
}

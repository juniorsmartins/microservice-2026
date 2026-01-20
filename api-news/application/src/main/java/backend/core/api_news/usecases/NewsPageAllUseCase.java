package backend.core.api_news.usecases;

import backend.core.api_news.dtos.NewsDto;
import backend.core.api_news.ports.input.NewsPageAllInputPort;
import backend.core.api_news.ports.output.NewsPageAllOutputPort;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class NewsPageAllUseCase implements NewsPageAllInputPort {

    private final NewsPageAllOutputPort newsPageAllOutputPort;

    public NewsPageAllUseCase(NewsPageAllOutputPort newsPageAllOutputPort) {
        this.newsPageAllOutputPort = newsPageAllOutputPort;
    }

    @Cacheable(value = "newsPageAll", key = "#pageable", condition = "#pageable.pageNumber <= 1", unless = "#result == null || #result.isEmpty()")
    @Override
    public Page<NewsDto> pageAll(Pageable pageable) {
        return newsPageAllOutputPort.pageAll(pageable);
    }
}

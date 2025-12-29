package backend.core.api_news.gateways;

import backend.core.api_news.dtos.NewsDto;
import backend.core.api_news.ports.output.NewsSaveOutputPort;
import backend.core.api_news.presenters.NewsPresenterPort;
import backend.core.api_news.repositories.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class NewsSaveGateway implements NewsSaveOutputPort {

    private final NewsRepository newsRepository;

    private final NewsPresenterPort newsPresenterPort;

    @Transactional
    @Override
    public NewsDto save(NewsDto dto) {

        var entity = newsPresenterPort.toEntity(dto);
        var saved = newsRepository.save(entity);
        return newsPresenterPort.toNewsDto(saved);
    }
}

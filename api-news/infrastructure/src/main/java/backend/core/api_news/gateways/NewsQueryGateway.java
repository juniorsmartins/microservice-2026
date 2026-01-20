package backend.core.api_news.gateways;

import backend.core.api_news.dtos.NewsDto;
import backend.core.api_news.ports.output.NewsFindByIdOutputPort;
import backend.core.api_news.presenters.NewsPresenterPort;
import backend.core.api_news.repositories.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class NewsQueryGateway implements NewsFindByIdOutputPort {

    private final NewsRepository newsRepository;

    private final NewsPresenterPort newsPresenterPort;

    @Transactional(readOnly = true)
    @Override
    public Optional<NewsDto> findById(final UUID id) {
        return newsRepository.findById(id)
                .map(newsPresenterPort::toNewsDto);
    }
}

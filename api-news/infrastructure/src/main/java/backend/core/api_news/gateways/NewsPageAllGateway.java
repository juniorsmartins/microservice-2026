package backend.core.api_news.gateways;

import backend.core.api_news.dtos.NewsDto;
import backend.core.api_news.ports.output.NewsPageAllOutputPort;
import backend.core.api_news.presenters.NewsPresenterPort;
import backend.core.api_news.repositories.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class NewsPageAllGateway implements NewsPageAllOutputPort {

    private final NewsRepository newsRepository;

    private final NewsPresenterPort newsPresenterPort;

    @Transactional(readOnly = true)
    @Override
    public Page<NewsDto> pageAll(Pageable pageable) {
        return newsRepository.findAll(pageable)
                .map(newsPresenterPort::toNewsDto);
    }
}

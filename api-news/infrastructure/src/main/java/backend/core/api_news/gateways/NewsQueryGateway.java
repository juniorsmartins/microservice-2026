package backend.core.api_news.gateways;

import backend.core.api_news.entities.NewsEntity;
import backend.core.api_news.repositories.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class NewsQueryGateway implements NewsQueryPort {

    private final NewsRepository newsRepository;

    @Transactional(readOnly = true)
    @Override
    public List<NewsEntity> findByTitleLike(String title) {
        return newsRepository.findByTitleLike(title);
    }
}

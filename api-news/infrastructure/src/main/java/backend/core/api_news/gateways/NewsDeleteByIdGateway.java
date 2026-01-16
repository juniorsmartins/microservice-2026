package backend.core.api_news.gateways;

import backend.core.api_news.ports.output.NewsDeleteByIdOutputPort;
import backend.core.api_news.repositories.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class NewsDeleteByIdGateway implements NewsDeleteByIdOutputPort {

    private final NewsRepository newsRepository;

    @Transactional
    @Override
    public void deleteById(final UUID id) {

        newsRepository.deleteById(id);
    }
}

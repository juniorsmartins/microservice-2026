package backend.core.api_news.ports.output;

import backend.core.api_news.dtos.NewsDto;

import java.util.Optional;
import java.util.UUID;

public interface NewsFindByIdOutputPort {

    Optional<NewsDto> findById(UUID id);
}

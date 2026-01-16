package backend.core.api_news.ports.input;

import backend.core.api_news.dtos.NewsDto;

import java.util.UUID;

public interface NewsFindByIdInputPort {

    NewsDto findById(UUID id);
}

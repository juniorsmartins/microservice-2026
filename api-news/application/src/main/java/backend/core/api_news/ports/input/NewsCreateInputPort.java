package backend.core.api_news.ports.input;

import backend.core.api_news.dtos.NewsDto;

public interface NewsCreateInputPort {

    NewsDto create(NewsDto dto);
}

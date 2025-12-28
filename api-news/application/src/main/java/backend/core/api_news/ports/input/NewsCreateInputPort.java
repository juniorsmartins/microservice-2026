package backend.core.api_news.ports.input;

import backend.core.api_news.dtos.NewsCreateDto;

public interface NewsCreateInputPort {

    NewsCreateDto create(NewsCreateDto dto);
}

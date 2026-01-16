package backend.core.api_news.ports.input;

import backend.core.api_news.dtos.NewsDto;

public interface NewsUpdateInputPort {

    NewsDto update(NewsDto dto);
}

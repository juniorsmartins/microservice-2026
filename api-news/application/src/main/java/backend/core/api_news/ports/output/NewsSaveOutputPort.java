package backend.core.api_news.ports.output;

import backend.core.api_news.dtos.NewsDto;

public interface NewsSaveOutputPort {

    NewsDto save(NewsDto dto);
}

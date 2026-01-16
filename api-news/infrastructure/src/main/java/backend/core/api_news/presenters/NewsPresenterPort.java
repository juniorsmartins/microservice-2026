package backend.core.api_news.presenters;

import backend.core.api_news.dtos.NewsDto;
import backend.core.api_news.dtos.requests.NewsRequest;
import backend.core.api_news.dtos.responses.NewsCreateResponse;
import backend.core.api_news.dtos.responses.NewsResponse;
import backend.core.api_news.entities.NewsEntity;

public interface NewsPresenterPort {

    NewsDto toNewsDto(NewsRequest request);

    NewsCreateResponse toNewsCreateResponse(NewsDto dto);

    NewsDto toNewsDto(NewsEntity entity);

    NewsResponse toNewsResponse(NewsEntity entity);

    NewsEntity toEntity(NewsDto dto);
}

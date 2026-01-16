package backend.core.api_news.presenters;

import backend.core.api_news.dtos.NewsDto;
import backend.core.api_news.dtos.requests.NewsRequest;
import backend.core.api_news.dtos.responses.NewsCreateResponse;
import backend.core.api_news.dtos.responses.NewsResponse;
import backend.core.api_news.entities.NewsEntity;

import java.util.UUID;

public interface NewsPresenterPort {

    NewsDto toNewsDto(NewsRequest request);

    NewsDto toNewsDto(UUID id, NewsRequest request);

    NewsDto toNewsDto(NewsEntity entity);

    NewsCreateResponse toNewsCreateResponse(NewsDto dto);

    NewsResponse toNewsResponse(NewsEntity entity);

    NewsResponse toNewsResponse(NewsDto dto);

    NewsEntity toEntity(NewsDto dto);
}

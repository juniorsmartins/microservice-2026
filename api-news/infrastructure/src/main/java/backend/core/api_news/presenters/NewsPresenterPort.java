package backend.core.api_news.presenters;

import backend.core.api_news.dtos.NewsDto;
import backend.core.api_news.dtos.requests.NewsCreateRequest;
import backend.core.api_news.dtos.requests.NewsUpdateRequest;
import backend.core.api_news.dtos.responses.NewsCreateResponse;
import backend.core.api_news.dtos.responses.NewsFindByIdResponse;
import backend.core.api_news.dtos.responses.NewsResponse;
import backend.core.api_news.dtos.responses.NewsUpdateResponse;
import backend.core.api_news.entities.NewsEntity;

import java.util.UUID;

public interface NewsPresenterPort {

    NewsDto toNewsDto(NewsCreateRequest request);

    NewsDto toNewsDto(UUID id, NewsUpdateRequest request);

    NewsDto toNewsDto(NewsEntity entity);

    NewsCreateResponse toNewsCreateResponse(NewsDto dto);

    NewsUpdateResponse toNewsUpdateResponse(NewsDto dto);

    NewsFindByIdResponse toNewsFindByIdResponse(NewsDto dto);

    NewsResponse toNewsResponse(NewsDto dto);

    NewsEntity toEntity(NewsDto dto);
}

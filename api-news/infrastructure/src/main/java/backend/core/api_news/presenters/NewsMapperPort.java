package backend.core.api_news.presenters;

import backend.core.api_news.dtos.NewsCreateDto;
import backend.core.api_news.dtos.NewsDto;
import backend.core.api_news.dtos.requests.NewsCreateRequestV1;
import backend.core.api_news.dtos.responses.NewsCreateResponseV1;
import backend.core.api_news.dtos.responses.NewsResponseV2;
import backend.core.api_news.entities.NewsEntity;

public interface NewsMapperPort {

    NewsCreateDto toNewsCreateDto(NewsCreateRequestV1 requestV1);

    NewsCreateResponseV1 toNewsCreateResponseV1(NewsCreateDto dto);

    NewsDto toNewsDto(NewsEntity entity);

    NewsResponseV2 toNewsResponseV2(NewsDto dto);

    NewsResponseV2 toNewsResponseV2(NewsEntity entity);
}

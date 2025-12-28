package backend.core.api_news.presenters;

import backend.core.api_news.dtos.NewsCreateDto;
import backend.core.api_news.dtos.requests.NewsCreateRequestV1;
import backend.core.api_news.dtos.responses.NewsCreateResponseV1;

public interface NewsMapperPort {

    NewsCreateDto toNewsCreateDto(NewsCreateRequestV1 requestV1);

    NewsCreateResponseV1 toNewsCreateResponseV1(NewsCreateDto dto);
}

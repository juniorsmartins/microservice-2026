package backend.core.api_news.presenters;

import backend.core.api_news.dtos.NewsCreateDto;
import backend.core.api_news.dtos.requests.NewsCreateRequestV1;
import backend.core.api_news.dtos.responses.NewsCreateResponseV1;
import org.springframework.stereotype.Component;

@Component
public class NewsMapper implements NewsMapperPort {

    @Override
    public NewsCreateDto toNewsCreateDto(NewsCreateRequestV1 requestV1) {
        return new NewsCreateDto(null, requestV1.hat(), requestV1.title(), requestV1.thinLine(), requestV1.text(), requestV1.font());
    }

    @Override
    public NewsCreateResponseV1 toNewsCreateResponseV1(NewsCreateDto dto) {
        return new NewsCreateResponseV1(dto.id(), dto.hat(), dto.title(), dto.thinLine(), dto.text(), dto.font());
    }
}

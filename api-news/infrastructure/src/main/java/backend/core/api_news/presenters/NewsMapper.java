package backend.core.api_news.presenters;

import backend.core.api_news.dtos.NewsCreateDto;
import backend.core.api_news.dtos.NewsDto;
import backend.core.api_news.dtos.requests.NewsCreateRequestV1;
import backend.core.api_news.dtos.responses.NewsCreateResponseV1;
import backend.core.api_news.dtos.responses.NewsResponseV2;
import backend.core.api_news.entities.NewsEntity;
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

    @Override
    public NewsDto toNewsDto(NewsEntity entity) {
        return new NewsDto(entity.getId(), entity.getHat(), entity.getTitle(), entity.getThinLine(), entity.getText(), entity.getFont());
    }

    @Override
    public NewsResponseV2 toNewsResponseV2(NewsDto dto) {
        return new NewsResponseV2(dto.id(), dto.hat(), dto.title(), dto.thinLine(), dto.text(), dto.font());
    }

    @Override
    public NewsResponseV2 toNewsResponseV2(NewsEntity entity) {
        return new NewsResponseV2(entity.getId(), entity.getHat(), entity.getTitle(), entity.getThinLine(), entity.getText(), entity.getFont());
    }
}

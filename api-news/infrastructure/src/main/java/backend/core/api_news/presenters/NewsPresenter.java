package backend.core.api_news.presenters;

import backend.core.api_news.dtos.NewsDto;
import backend.core.api_news.dtos.requests.NewsCreateRequest;
import backend.core.api_news.dtos.responses.NewsCreateResponse;
import backend.core.api_news.dtos.responses.NewsResponse;
import backend.core.api_news.entities.NewsEntity;
import org.springframework.stereotype.Component;

@Component
public class NewsPresenter implements NewsPresenterPort {

    @Override
    public NewsDto toNewsDto(NewsCreateRequest requestV1) {
        return new NewsDto(null, requestV1.hat(), requestV1.title(), requestV1.thinLine(), requestV1.text(), requestV1.font());
    }

    @Override
    public NewsCreateResponse toNewsCreateResponse(NewsDto dto) {
        return new NewsCreateResponse(dto.id(), dto.hat(), dto.title(), dto.thinLine(), dto.text(), dto.font());
    }

    @Override
    public NewsDto toNewsDto(NewsEntity entity) {
        return new NewsDto(entity.getId(), entity.getHat(), entity.getTitle(), entity.getThinLine(), entity.getText(), entity.getFont());
    }

    @Override
    public NewsResponse toNewsResponse(NewsEntity entity) {
        return new NewsResponse(entity.getId(), entity.getHat(), entity.getTitle(), entity.getThinLine(), entity.getText(), entity.getFont());
    }

    @Override
    public NewsEntity toEntity(NewsDto dto) {
        return new NewsEntity(dto.id(), dto.hat(), dto.title(), dto.thinLine(), dto.text(), dto.font());
    }
}

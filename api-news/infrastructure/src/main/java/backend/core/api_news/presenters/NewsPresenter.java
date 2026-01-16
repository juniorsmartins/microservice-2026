package backend.core.api_news.presenters;

import backend.core.api_news.dtos.NewsDto;
import backend.core.api_news.dtos.requests.NewsRequest;
import backend.core.api_news.dtos.responses.NewsCreateResponse;
import backend.core.api_news.dtos.responses.NewsResponse;
import backend.core.api_news.entities.NewsEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class NewsPresenter implements NewsPresenterPort {

    @Override
    public NewsDto toNewsDto(NewsRequest request) {
        return new NewsDto(null, request.hat(), request.title(), request.thinLine(), request.text(), request.author(), request.font());
    }

    @Override
    public NewsDto toNewsDto(UUID id, NewsRequest request) {
        return new NewsDto(id, request.hat(), request.title(), request.thinLine(), request.text(), request.author(), request.font());
    }

    @Override
    public NewsDto toNewsDto(NewsEntity entity) {
        return new NewsDto(entity.getId(), entity.getHat(), entity.getTitle(), entity.getThinLine(), entity.getText(), entity.getAuthor(), entity.getFont());
    }

    @Override
    public NewsCreateResponse toNewsCreateResponse(NewsDto dto) {
        return new NewsCreateResponse(dto.id(), dto.hat(), dto.title(), dto.thinLine(), dto.text(), dto.author(), dto.font());
    }

    @Override
    public NewsResponse toNewsResponse(NewsEntity entity) {
        return new NewsResponse(entity.getId(), entity.getHat(), entity.getTitle(), entity.getThinLine(), entity.getText(), entity.getAuthor(), entity.getFont());
    }

    @Override
    public NewsResponse toNewsResponse(NewsDto dto) {
        return new NewsResponse(dto.id(), dto.hat(), dto.title(), dto.thinLine(), dto.text(), dto.author(), dto.font());
    }

    @Override
    public NewsEntity toEntity(NewsDto dto) {
        return new NewsEntity(dto.id(), dto.hat(), dto.title(), dto.thinLine(), dto.text(), dto.author(), dto.font());
    }
}

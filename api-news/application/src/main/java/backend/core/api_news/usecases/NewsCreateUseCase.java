package backend.core.api_news.usecases;

import backend.core.api_news.dtos.NewsCreateDto;
import backend.core.api_news.ports.input.NewsCreateInputPort;

public class NewsCreateUseCase implements NewsCreateInputPort {

    @Override
    public NewsCreateDto create(NewsCreateDto dto) {
        return dto;
    }
}

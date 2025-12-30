package backend.core.api_news.usecases;

import backend.core.api_news.dtos.NewsDto;
import backend.core.api_news.ports.input.NewsCreateInputPort;
import backend.core.api_news.ports.output.NewsSaveOutputPort;

public class NewsCreateUseCase implements NewsCreateInputPort {

    private final NewsSaveOutputPort newsSaveOutputPort;

    public NewsCreateUseCase(NewsSaveOutputPort newsSaveOutputPort) {
        this.newsSaveOutputPort = newsSaveOutputPort;
    }

    @Override
    public NewsDto create(NewsDto dto) {
        return newsSaveOutputPort.save(dto);
    }
}

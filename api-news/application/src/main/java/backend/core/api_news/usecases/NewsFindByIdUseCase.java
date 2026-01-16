package backend.core.api_news.usecases;

import backend.core.api_news.dtos.NewsDto;
import backend.core.api_news.ports.input.NewsFindByIdInputPort;
import backend.core.api_news.ports.output.NewsFindByIdOutputPort;

import java.util.UUID;

public class NewsFindByIdUseCase implements NewsFindByIdInputPort {

    private final NewsFindByIdOutputPort newsFindByIdOutputPort;

    public NewsFindByIdUseCase(NewsFindByIdOutputPort newsFindByIdOutputPort) {
        this.newsFindByIdOutputPort = newsFindByIdOutputPort;
    }

    @Override
    public NewsDto findById(final UUID id) {

        return newsFindByIdOutputPort.findById(id)
                .orElseThrow(() -> new RuntimeException("News with id " + id + " not found"));
    }
}

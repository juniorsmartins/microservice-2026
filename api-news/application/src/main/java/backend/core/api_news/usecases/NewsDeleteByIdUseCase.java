package backend.core.api_news.usecases;

import backend.core.api_news.ports.input.NewsDeleteByIdInputPort;
import backend.core.api_news.ports.output.NewsDeleteByIdOutputPort;
import backend.core.api_news.ports.output.NewsFindByIdOutputPort;

import java.util.UUID;

public class NewsDeleteByIdUseCase implements NewsDeleteByIdInputPort {

    private final NewsFindByIdOutputPort newsFindByIdOutputPort;

    private final NewsDeleteByIdOutputPort newsDeleteByIdOutputPort;

    public NewsDeleteByIdUseCase(NewsFindByIdOutputPort newsFindByIdOutputPort,
                                 NewsDeleteByIdOutputPort newsDeleteByIdOutputPort) {
        this.newsFindByIdOutputPort = newsFindByIdOutputPort;
        this.newsDeleteByIdOutputPort = newsDeleteByIdOutputPort;
    }

    @Override
    public void deleteById(UUID id) {

        newsFindByIdOutputPort.findById(id)
                .ifPresentOrElse(news -> newsDeleteByIdOutputPort.deleteById(news.id()),
                        () -> {
                            throw new IllegalArgumentException("News with id " + id + " not found");
                        }
                );

    }
}

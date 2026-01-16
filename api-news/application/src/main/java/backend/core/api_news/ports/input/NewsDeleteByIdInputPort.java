package backend.core.api_news.ports.input;

import java.util.UUID;

public interface NewsDeleteByIdInputPort {

    void deleteById(UUID id);
}

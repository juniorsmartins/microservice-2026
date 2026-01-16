package backend.core.api_news.ports.output;

import java.util.UUID;

public interface NewsDeleteByIdOutputPort {

    void deleteById(UUID id);
}

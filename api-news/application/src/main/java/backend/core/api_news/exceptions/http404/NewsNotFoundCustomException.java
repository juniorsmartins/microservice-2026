package backend.core.api_news.exceptions.http404;

import java.io.Serial;
import java.util.UUID;

public final class NewsNotFoundCustomException extends ResourceNotFoundCustomException {

    @Serial
    private static final long serialVersionUID = 1L;

    public NewsNotFoundCustomException(final UUID newsId) {
        super("exception.resource.not-found.news", newsId.toString());
    }
}

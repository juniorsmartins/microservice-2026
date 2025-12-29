package backend.core.api_news.dtos.responses;

import java.util.UUID;

public record NewsResponse(

        UUID id,
        String hat,
        String title,
        String thinLine,
        String text,
        String font
) {
}

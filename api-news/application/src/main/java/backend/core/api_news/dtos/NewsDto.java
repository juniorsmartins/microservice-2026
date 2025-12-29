package backend.core.api_news.dtos;

import java.util.UUID;

public record NewsDto(

        UUID id,
        String hat,
        String title,
        String thinLine,
        String text,
        String font
) {
}

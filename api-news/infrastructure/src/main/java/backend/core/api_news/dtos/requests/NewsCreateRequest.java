package backend.core.api_news.dtos.requests;

public record NewsCreateRequest(

        String hat,
        String title,
        String thinLine,
        String text,
        String font
) {
}

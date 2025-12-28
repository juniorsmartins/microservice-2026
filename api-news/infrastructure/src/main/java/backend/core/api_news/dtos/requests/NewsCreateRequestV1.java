package backend.core.api_news.dtos.requests;

public record NewsCreateRequestV1(

        String hat,
        String title,
        String thinLine,
        String text,
        String font
) {
}

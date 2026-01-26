package backend.ia.infrastructure.dtos.response;

import java.util.List;

public record TweetResponse(

        String content,

        List<String> hashTag
) {
}

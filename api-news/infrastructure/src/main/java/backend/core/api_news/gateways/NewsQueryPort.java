package backend.core.api_news.gateways;

import java.util.List;

public interface NewsQueryPort {

    List<NewsEntity> findByTitleLike(String title);
}

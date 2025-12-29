package backend.core.api_news.gateways;

import backend.core.api_news.entities.NewsEntity;

import java.util.List;

public interface NewsQueryPort {

    List<NewsEntity> findByTitleLike(String title);
}

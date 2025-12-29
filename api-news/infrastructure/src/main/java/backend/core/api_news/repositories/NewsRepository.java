package backend.core.api_news.repositories;

import backend.core.api_news.entities.NewsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface NewsRepository extends JpaRepository<NewsEntity, UUID> {

    @Query(value = "SELECT * FROM news n WHERE n.title LIKE CONCAT('%', :title, '%')", nativeQuery = true)
    List<NewsEntity> findByTitleLike(@Param("title") String title);
}

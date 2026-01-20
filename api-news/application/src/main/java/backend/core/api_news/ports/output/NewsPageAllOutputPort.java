package backend.core.api_news.ports.output;

import backend.core.api_news.dtos.NewsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NewsPageAllOutputPort {

    Page<NewsDto> pageAll(Pageable pageable);
}

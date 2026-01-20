package backend.core.api_news.ports.input;

import backend.core.api_news.dtos.NewsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NewsPageAllInputPort {

    Page<NewsDto> pageAll(Pageable pageable);
}

package backend.core.api_news.controllers;

import backend.core.api_news.dtos.requests.NewsCreateRequest;
import backend.core.api_news.dtos.responses.NewsCreateResponse;
import backend.core.api_news.dtos.responses.NewsResponse;
import backend.core.api_news.ports.input.NewsCreateInputPort;
import backend.core.api_news.gateways.NewsQueryPort;
import backend.core.api_news.presenters.NewsPresenterPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Slf4j
@NullMarked
@RestController
@RequestMapping(path = {"/api/"})
@RequiredArgsConstructor
public class NewsController {

    private final NewsCreateInputPort newsCreateInputPort;

    private final NewsQueryPort newsQueryPort;

    private final NewsPresenterPort newsPresenterPort;

    @PostMapping(value = "/{version}/news", version = "1.0")
    public ResponseEntity<NewsCreateResponse> create(@RequestBody NewsCreateRequest requestV1) {

        var response = Optional.of(requestV1)
                .map(newsPresenterPort::toNewsDto)
                .map(newsCreateInputPort::create)
                .map(newsPresenterPort::toNewsCreateResponse)
                .orElseThrow();

        return ResponseEntity
                .created(URI.create("/api/1.0/news/" + response.id()))
                .body(response);
    }

    @GetMapping(value = "/{version}/news", version = "1.0")
    public List<NewsResponse> findByTitleLike(@RequestParam(name = "title") String title) {

        return newsQueryPort.findByTitleLike(title)
                .stream()
                .map(newsPresenterPort::toNewsResponse)
                .toList();
    }
}

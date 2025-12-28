package backend.core.api_news.controllers;

import backend.core.api_news.dtos.requests.NewsCreateRequestV1;
import backend.core.api_news.dtos.responses.NewsCreateResponseV1;
import backend.core.api_news.dtos.responses.NewsResponseV1;
import backend.core.api_news.dtos.responses.NewsResponseV2;
import backend.core.api_news.ports.input.NewsCreateInputPort;
import backend.core.api_news.presenters.NewsMapperPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(path = {"/api/"})
@RequiredArgsConstructor
public class NewsController {

    private final NewsCreateInputPort newsCreateInputPort;

    private final NewsMapperPort newsMapperPort;

    @GetMapping(value = "/{version}/news", version = "1.0")
    public List<NewsResponseV1> findAll() {
        log.info("\n\n version 1.0 \n\n");
        return List.of(new NewsResponseV1("Esporte", "Cowboys vencem Lions",
                "Um touchdown e um extra point para mais", "xpto"));
    }

    @GetMapping(value = "/{version}/news", version = "2.0")
    public List<NewsResponseV2> findAllV2() {
        log.info("\n\n version 2.0 \n\n");
        return List.of(new NewsResponseV2("Esporte", "Cowboys vencem Lions",
                "Um touchdown e um extra point para mais", "xpto", "Gov"));
    }

    @PostMapping(value = "/{version}/news", version = "1.0")
    public ResponseEntity<NewsCreateResponseV1> createV1(@RequestBody NewsCreateRequestV1 requestV1) {

        var response = Optional.of(requestV1)
                .map(newsMapperPort::toNewsCreateDto)
                .map(newsCreateInputPort::create)
                .map(newsMapperPort::toNewsCreateResponseV1)
                .orElseThrow();

        return ResponseEntity
                .created(URI.create("/1.0/news/" + response.id()))
                .body(response);
    }
}

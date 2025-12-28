package backend.core.api_news.controllers;

import backend.core.api_news.dtos.responses.NewsResponseV1;
import backend.core.api_news.dtos.responses.NewsResponseV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = {"/api/"})
@RequiredArgsConstructor
public class NewsController {

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
}

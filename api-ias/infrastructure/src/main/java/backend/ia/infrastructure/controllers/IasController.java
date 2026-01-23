package backend.ia.infrastructure.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Ias", description = "Controlador do recurso de Ias.")
@Slf4j
@NullMarked
@RestController
@RequestMapping(path = {"/api/"})
@RequiredArgsConstructor
public class IasController {

}

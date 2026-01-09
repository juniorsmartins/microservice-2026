package backend.ia.infrastructure.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Tag(name = "Ias", description = "Controlador do recurso de Ias.")
@Slf4j
@NullMarked
@RestController
@RequestMapping(path = {"/api/"})
@RequiredArgsConstructor
public class IasController {

    @GetMapping(value = "/{version}/ias/hostcheck", version = "1.0")
    public String checkHost() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostName() + " - " + InetAddress.getLocalHost().getHostAddress();
    }

}

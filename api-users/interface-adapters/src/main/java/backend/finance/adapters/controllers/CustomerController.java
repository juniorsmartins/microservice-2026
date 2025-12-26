package backend.finance.adapters.controllers;

import backend.finance.adapters.gateways.CustomerPagePort;
import backend.finance.application.dtos.request.CustomerRequest;
import backend.finance.application.dtos.response.CustomerAllResponse;
import backend.finance.application.dtos.response.CustomerResponse;
import backend.finance.application.exceptions.http404.CustomerNotFoundCustomException;
import backend.finance.application.exceptions.http404.RoleNotFoundCustomException;
import backend.finance.application.exceptions.http409.EmailConflictRulesCustomException;
import backend.finance.application.exceptions.http409.UsernameConflictRulesCustomException;
import backend.finance.application.ports.input.CustomerCreateInputPort;
import backend.finance.application.ports.input.CustomerDisableInputPort;
import backend.finance.application.ports.input.CustomerQueryInputPort;
import backend.finance.application.ports.input.CustomerUpdateInputPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(path = {CustomerController.URI_CUSTOMERS})
@RequiredArgsConstructor
public class CustomerController {

    protected static final String URI_CUSTOMERS = "/v1/customers";

    private final CustomerCreateInputPort customerCreateInputPort;

    private final CustomerUpdateInputPort customerUpdateInputPort;

    private final CustomerDisableInputPort customerDisableInputPort;

    private final CustomerQueryInputPort customerQueryInputPort;

    private final CustomerPagePort customerPagePort;

    @PostMapping
    public ResponseEntity<CustomerResponse> create(@RequestBody CustomerRequest request) {

        var created = customerCreateInputPort.create(request);

        return ResponseEntity
                .created(URI.create(URI_CUSTOMERS + "/" + created.id()))
                .body(created);
    }

    @PutMapping(path = "/{id}")
    @Retryable(
            excludes = {CustomerNotFoundCustomException.class, EmailConflictRulesCustomException.class,
                    UsernameConflictRulesCustomException.class, RoleNotFoundCustomException.class},
            maxRetries = 2,
            jitter = 10,
            delay = 1000,
            multiplier = 2
    )
    public ResponseEntity<CustomerResponse> update(@PathVariable(name = "id") final UUID id, @RequestBody CustomerRequest request) {

        var updated = customerUpdateInputPort.update(id, request);

        return ResponseEntity
                .ok()
                .body(updated);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> disableById(@PathVariable(name = "id") final UUID id) {

        customerDisableInputPort.disableById(id);

        return ResponseEntity
                .noContent()
                .build();
    }

    @GetMapping(path = "/{id}")
    @Retryable(
            excludes = {CustomerNotFoundCustomException.class},
            maxRetries = 4, // Número máximo de tentativas de repetição em caso de falha.
            jitter = 10, // Variação aleatória adicionada ao tempo de espera para evitar picos de carga. Fator de "desfocagem" (blur) para evitar a sincronização de rede. Se usar valor 10 (significa geralmente interpretado como +/- 10% de variação)
            delay = 1000, // Primeiro tempo de espera antes de tentar novamente (milliseconds).
            multiplier = 2 // Fator pelo qual o tempo de espera é multiplicado a cada tentativa subsequente. Um multiplicador para o atraso da próxima tentativa de repetição, aplicado ao atraso anterior (a partir de delay()) e também ao jitter() aplicável a cada tentativa.
    )
    public ResponseEntity<CustomerResponse> findById(@PathVariable(name = "id") final UUID id) {

        var response = customerQueryInputPort.findActiveById(id);

        return ResponseEntity
                .ok()
                .body(response);
    }

    @GetMapping
    @Retryable(
            maxRetries = 4, // Número máximo de tentativas de repetição em caso de falha.
            jitter = 10, // Variação aleatória adicionada ao tempo de espera para evitar picos de carga. Fator de "desfocagem" (blur) para evitar a sincronização de rede. Se usar valor 10 (significa geralmente interpretado como +/- 10% de variação)
            delay = 1000, // Primeiro tempo de espera antes de tentar novamente (milliseconds).
            multiplier = 2 // Fator pelo qual o tempo de espera é multiplicado a cada tentativa subsequente. Um multiplicador para o atraso da próxima tentativa de repetição, aplicado ao atraso anterior (a partir de delay()) e também ao jitter() aplicável a cada tentativa.
    )
    public ResponseEntity<Page<CustomerAllResponse>> pageAll(
            @PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC, page = 0, size = 5) Pageable paginacao) {

        var responsePage = customerPagePort.pageAll(paginacao);

        return ResponseEntity
                .ok()
                .body(responsePage);
    }
}

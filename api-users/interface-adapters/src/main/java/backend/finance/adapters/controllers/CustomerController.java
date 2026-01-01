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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@Tag(name = "CustomerController", description = "Controlador do recurso Cliente.")
@Slf4j
@NullMarked
@RestController
@RequestMapping(path = {"/api/"})
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerCreateInputPort customerCreateInputPort;

    private final CustomerUpdateInputPort customerUpdateInputPort;

    private final CustomerDisableInputPort customerDisableInputPort;

    private final CustomerQueryInputPort customerQueryInputPort;

    private final CustomerPagePort customerPagePort;

    @Operation(summary = "Cadastrar", description = "Recurso para criar novos clientes.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Created - recurso cadastrado com sucesso.",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CustomerResponse.class))}
                    ),
                    @ApiResponse(responseCode = "400", description = "Bad Request - requisição mal formulada.",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}
                    ),
                    @ApiResponse(responseCode = "409", description = "Conflict - violação de regras de negócio.",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}
                    ),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error - situação inesperada no servidor.",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}
                    )
            }
    )
    @PostMapping(value = "/{version}/customers", version = "1.0")
    public ResponseEntity<CustomerResponse> create(
            @Parameter(name = "CustomerRequest", description = "Estrutura de transporte de entrada de dados.", required = true)
            @RequestBody CustomerRequest request) {

        var created = customerCreateInputPort.create(request);

        return ResponseEntity
                .created(URI.create("/api/1.0/customers/" + created.id()))
                .body(created);
    }

    @Operation(summary = "Atualizar", description = "Recurso para atualizar clientes.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK - requisição bem sucedida e com retorno.",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CustomerResponse.class))}
                    ),
                    @ApiResponse(responseCode = "400", description = "Bad Request - requisição mal formulada.",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}
                    ),
                    @ApiResponse(responseCode = "404", description = "Not Found - recurso não encontrado.",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}
                    ),
                    @ApiResponse(responseCode = "409", description = "Conflict - violação de regras de negócio.",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}
                    ),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error - situação inesperada no servidor.",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}
                    )
            }
    )
    @PutMapping(value = "/{version}/customers/{id}", version = "1.0")
    @Retryable(
            excludes = {CustomerNotFoundCustomException.class, EmailConflictRulesCustomException.class,
                    UsernameConflictRulesCustomException.class, RoleNotFoundCustomException.class},
            maxRetries = 2,
            jitter = 10,
            delay = 1000,
            multiplier = 2
    )
    public ResponseEntity<CustomerResponse> update(
            @Parameter(name = "id", description = "Identificador único do recurso.", example = "034eb74c-69ee-4bd4-a064-5c4cc5e9e748", required = true)
            @PathVariable(name = "id") final UUID id,
            @Parameter(name = "CustomerRequest", description = "Estrutura de transporte de entrada de dados.", required = true)
            @RequestBody CustomerRequest request) {

        var updated = customerUpdateInputPort.update(id, request);

        return ResponseEntity
                .ok()
                .body(updated);
    }

    @Operation(summary = "Desativar", description = "Recurso para desativar clientes.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "No Content - requisição bem sucedida e sem retorno.",
                            content = {@Content(mediaType = "application/json")}
                    ),
                    @ApiResponse(responseCode = "400", description = "Bad Request - requisição mal formulada.",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}
                    ),
                    @ApiResponse(responseCode = "404", description = "Not Found - recurso não encontrado.",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}
                    ),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error - situação inesperada no servidor.",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}
                    )
            }
    )
    @DeleteMapping(value = "/{version}/customers/{id}", version = "1.0")
    public ResponseEntity<Void> disableById(
            @Parameter(name = "id", description = "Identificador único do recurso.", example = "034eb74c-69ee-4bd4-a064-5c4cc5e9e748", required = true)
            @PathVariable(name = "id") final UUID id) {

        customerDisableInputPort.disableById(id);

        return ResponseEntity
                .noContent()
                .build();
    }

    @Operation(summary = "Consultar por ID", description = "Recurso para consultar clientes por ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK - requisição bem sucedida e com retorno.",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CustomerResponse.class))}
                    ),
                    @ApiResponse(responseCode = "400", description = "Bad Request - requisição mal formulada.",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}
                    ),
                    @ApiResponse(responseCode = "404", description = "Not Found - recurso não encontrado.",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}
                    ),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error - situação inesperada no servidor.",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}
                    )
            }
    )
    @GetMapping(value = "/{version}/customers/{id}", version = "1.0")
    @Retryable(
            excludes = {CustomerNotFoundCustomException.class},
            maxRetries = 4, // Número máximo de tentativas de repetição em caso de falha.
            jitter = 10, // Variação aleatória adicionada ao tempo de espera para evitar picos de carga. Fator de "desfocagem" (blur) para evitar a sincronização de rede. Se usar valor 10 (significa geralmente interpretado como +/- 10% de variação)
            delay = 1000, // Primeiro tempo de espera antes de tentar novamente (milliseconds).
            multiplier = 2 // Fator pelo qual o tempo de espera é multiplicado a cada tentativa subsequente. Um multiplicador para o atraso da próxima tentativa de repetição, aplicado ao atraso anterior (a partir de delay()) e também ao jitter() aplicável a cada tentativa.
    )
    public ResponseEntity<CustomerResponse> findById(
            @Parameter(name = "id", description = "Identificador único do recurso.", example = "034eb74c-69ee-4bd4-a064-5c4cc5e9e748", required = true)
            @PathVariable(name = "id") final UUID id) {

        var response = customerQueryInputPort.findActiveById(id);

        return ResponseEntity
                .ok()
                .body(response);
    }

    @Operation(summary = "Paginar todos", description = "Recurso para buscar todos os clientes paginados.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK - requisição bem sucedida e com retorno.",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CustomerResponse.class))}
                    ),
                    @ApiResponse(responseCode = "400", description = "Bad Request - requisição mal formulada.",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}
                    ),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error - situação inesperada no servidor.",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}
                    )
            }
    )
    @GetMapping(value = "/{version}/customers", version = "1.0")
    @Retryable(
            maxRetries = 4, // Número máximo de tentativas de repetição em caso de falha.
            jitter = 10, // Variação aleatória adicionada ao tempo de espera para evitar picos de carga. Fator de "desfocagem" (blur) para evitar a sincronização de rede. Se usar valor 10 (significa geralmente interpretado como +/- 10% de variação)
            delay = 1000, // Primeiro tempo de espera antes de tentar novamente (milliseconds).
            multiplier = 2 // Fator pelo qual o tempo de espera é multiplicado a cada tentativa subsequente. Um multiplicador para o atraso da próxima tentativa de repetição, aplicado ao atraso anterior (a partir de delay()) e também ao jitter() aplicável a cada tentativa.
    )
    public ResponseEntity<Page<CustomerAllResponse>> pageAll(
            @ParameterObject
            @PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC, page = 0, size = 5) final Pageable paginacao) {

        var responsePage = customerPagePort.pageAll(paginacao);

        return ResponseEntity
                .ok()
                .body(responsePage);
    }
}

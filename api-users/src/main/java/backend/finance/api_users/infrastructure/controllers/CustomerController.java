package backend.finance.api_users.infrastructure.controllers;

import backend.finance.api_users.application.configs.exception.http404.CustomerNotFoundCustomException;
import backend.finance.api_users.application.configs.mensageria.producer.Producer;
import backend.finance.api_users.application.dtos.input.CustomerRequest;
import backend.finance.api_users.application.dtos.output.CustomerResponse;
import backend.finance.api_users.infrastructure.ports.input.CustomerCreateInputPort;
import backend.finance.api_users.infrastructure.ports.input.CustomerDeleteInputPort;
import backend.finance.api_users.infrastructure.ports.input.CustomerUpdateInputPort;
import backend.finance.api_users.infrastructure.ports.output.CustomerQueryOutputPort;
import backend.finance.api_users.infrastructure.presenters.CustomerPresenter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping(path = {CustomerController.URI_CUSTOMERS})
@RequiredArgsConstructor
public class CustomerController {

    protected static final String URI_CUSTOMERS = "/v1/customers";

    private final CustomerCreateInputPort customerCreateInputPort;

    private final CustomerUpdateInputPort customerUpdateInputPort;

    private final CustomerDeleteInputPort customerDeleteInputPort;

    private final CustomerQueryOutputPort customerQueryOutputPort;

    private final CustomerPresenter customerPresenter;

    private final Producer producer;

    @PostMapping
    public ResponseEntity<CustomerResponse> create(@RequestBody @Valid CustomerRequest request) {

        var created = customerCreateInputPort.create(request);
        var response = customerPresenter.toResponse(created);

        var message = customerPresenter.toMessage(response);
        producer.sendEventCreateCustomer(message);

        return ResponseEntity
                .created(URI.create(URI_CUSTOMERS + "/" + response.id()))
                .body(response);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<CustomerResponse> update(@PathVariable(name = "id") final UUID id, @RequestBody @Valid CustomerRequest request) {

        var customer = customerUpdateInputPort.update(id, request);
        var response = customerPresenter.toResponse(customer);

        return ResponseEntity
                .ok()
                .body(response);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> disableById(@PathVariable(name = "id") final UUID id) {

        customerDeleteInputPort.disableById(id);

        return ResponseEntity
                .noContent()
                .build();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<CustomerResponse> findById(@PathVariable(name = "id") final UUID id) {

        return customerQueryOutputPort.findByIdAndActiveTrue(id)
                .map(customerPresenter::toResponse)
                .map(dto -> ResponseEntity.ok().body(dto))
                .orElseThrow(() -> new CustomerNotFoundCustomException(id));
    }
}

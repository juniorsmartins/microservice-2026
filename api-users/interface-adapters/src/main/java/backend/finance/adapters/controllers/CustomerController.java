package backend.finance.adapters.controllers;

import backend.finance.adapters.presenters.CustomerPresenter;
import backend.finance.application.dtos.request.CustomerRequest;
import backend.finance.application.dtos.response.CustomerResponse;
import backend.finance.application.ports.input.CustomerCreateInputPort;
import backend.finance.application.ports.input.CustomerDisableInputPort;
import backend.finance.application.ports.input.CustomerQueryInputPort;
import backend.finance.application.ports.input.CustomerUpdateInputPort;
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

    private final CustomerDisableInputPort customerDisableInputPort;

    private final CustomerQueryInputPort customerQueryInputPort;

    private final CustomerPresenter customerPresenter;

    @PostMapping
    public ResponseEntity<CustomerResponse> create(@RequestBody CustomerRequest request) {

        var created = customerCreateInputPort.create(request);

        return ResponseEntity
                .created(URI.create(URI_CUSTOMERS + "/" + created.id()))
                .body(created);
    }

    @PutMapping(path = "/{id}")
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
    public ResponseEntity<CustomerResponse> findById(@PathVariable(name = "id") final UUID id) {

        var response = customerQueryInputPort.findByIdAndActiveTrue(id);

        return ResponseEntity
                .ok()
                .body(response);
    }
}

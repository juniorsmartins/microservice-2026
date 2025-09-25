package backend.finance.api_user.infrastructure.controllers;

import backend.finance.api_user.application.configs.exception.http404.CustomerNotFoundCustomException;
import backend.finance.api_user.application.dtos.input.CustomerRequest;
import backend.finance.api_user.application.dtos.output.CustomerResponse;
import backend.finance.api_user.infrastructure.ports.input.CustomerInputPort;
import backend.finance.api_user.infrastructure.ports.output.CustomerOutputPort;
import backend.finance.api_user.infrastructure.presenters.CustomerPresenter;
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

    protected static final String URI_CUSTOMERS = "/api/v1/customers";

    private final CustomerInputPort customerInputPort;

    private final CustomerOutputPort customerOutputPort;

    @PostMapping
    public ResponseEntity<CustomerResponse> create(@RequestBody @Valid CustomerRequest request) {

        var dto = customerInputPort.create(request, customerOutputPort);
        var response = CustomerPresenter.toCustomerResponse(dto);

        return ResponseEntity
                .created(URI.create(URI_CUSTOMERS + "/" + response.id()))
                .body(response);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<CustomerResponse> update(@PathVariable(name = "id") final UUID id, @RequestBody @Valid CustomerRequest request) {

        var customerDto = customerInputPort.update(id, request, customerOutputPort);
        var response = CustomerPresenter.toCustomerResponse(customerDto);

        return ResponseEntity
                .ok()
                .body(response);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable(name = "id") final UUID id) {
        customerInputPort.deleteById(id, customerOutputPort);
        return ResponseEntity
                .noContent()
                .build();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<CustomerResponse> findById(@PathVariable(name = "id") final UUID id) {

        var response = customerOutputPort.findById(id)
                .map(CustomerPresenter::toCustomerResponse)
                .orElseThrow(() -> new CustomerNotFoundCustomException(id));

        return ResponseEntity
                .ok()
                .body(response);
    }
}

package backend.finance.api_users.interface_adapters.controllers;

import backend.finance.api_users.application_business_rules.dtos.input.CustomerRequest;
import backend.finance.api_users.application_business_rules.dtos.output.CustomerResponse;
import backend.finance.api_users.application_business_rules.exception.http404.CustomerNotFoundCustomException;
import backend.finance.api_users.application_business_rules.ports.input.CustomerCreateInputPort;
import backend.finance.api_users.application_business_rules.ports.input.CustomerDeleteInputPort;
import backend.finance.api_users.application_business_rules.ports.input.CustomerUpdateInputPort;
import backend.finance.api_users.application_business_rules.ports.output.CustomerQueryOutputPort;
import backend.finance.api_users.interface_adapters.mensageria.producer.CustomerEventPublisher;
import backend.finance.api_users.interface_adapters.presenters.CustomerPresenter;
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

    private final CustomerEventPublisher eventPublisher;

    @PostMapping
    public ResponseEntity<CustomerResponse> create(@RequestBody @Valid CustomerRequest request) {

        var created = customerCreateInputPort.create(request);
        var response = customerPresenter.toResponse(created);

        eventPublisher.sendEventCreateCustomer(response);

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

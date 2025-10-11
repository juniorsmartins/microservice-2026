package backend.finance.api_user.infrastructure.controllers;

import backend.finance.api.user.CustomerKafka;
import backend.finance.api_user.application.configs.exception.http404.CustomerNotFoundCustomException;
import backend.finance.api_user.application.configs.kafka.KafkaProducer;
import backend.finance.api_user.application.dtos.input.CustomerRequest;
import backend.finance.api_user.application.dtos.output.CustomerResponse;
import backend.finance.api_user.infrastructure.ports.input.CustomerCreateInputPort;
import backend.finance.api_user.infrastructure.ports.input.CustomerDeleteInputPort;
import backend.finance.api_user.infrastructure.ports.input.CustomerUpdateInputPort;
import backend.finance.api_user.infrastructure.ports.output.CustomerQueryOutputPort;
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

    private final CustomerCreateInputPort customerCreateInputPort;

    private final CustomerUpdateInputPort customerUpdateInputPort;

    private final CustomerDeleteInputPort customerDeleteInputPort;

    private final CustomerQueryOutputPort customerQueryOutputPort;

    private final CustomerPresenter customerPresenter;

    private final KafkaProducer kafkaProducer;

    @PostMapping
    public ResponseEntity<CustomerResponse> create(@RequestBody @Valid CustomerRequest request) {

        var dto = customerCreateInputPort.create(request);
        var response = customerPresenter.toCustomerResponse(dto);
        kafkaProducer.enviarEvento(new CustomerKafka(response.id().toString(), response.name(), response.email()));

        return ResponseEntity
                .created(URI.create(URI_CUSTOMERS + "/" + response.id()))
                .body(response);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<CustomerResponse> update(@PathVariable(name = "id") final UUID id, @RequestBody @Valid CustomerRequest request) {

        var customerDto = customerUpdateInputPort.update(id, request);
        var response = customerPresenter.toCustomerResponse(customerDto);

        return ResponseEntity
                .ok()
                .body(response);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable(name = "id") final UUID id) {

        customerDeleteInputPort.deleteById(id);
        return ResponseEntity
                .noContent()
                .build();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<CustomerResponse> findById(@PathVariable(name = "id") final UUID id) {

        return customerQueryOutputPort.findById(id)
                .map(customerPresenter::toCustomerResponse)
                .map(dto -> ResponseEntity.ok().body(dto))
                .orElseThrow(() -> new CustomerNotFoundCustomException(id));
    }
}

package backend.finance.api_user.infrastructure.controllers;

import backend.finance.api_user.application.dtos.input.CustomerRequest;
import backend.finance.api_user.application.dtos.output.CustomerResponse;
import backend.finance.api_user.infrastructure.ports.input.CustomerInputPort;
import backend.finance.api_user.infrastructure.ports.output.CustomerOutputPort;
import backend.finance.api_user.infrastructure.ports.output.UserOutputPort;
import backend.finance.api_user.infrastructure.presenters.CustomerPresenter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping(path = {CustomerController.URI_CUSTOMERS})
@RequiredArgsConstructor
public class CustomerController {

    protected static final String URI_CUSTOMERS = "/api/v1/customers";

    private final CustomerInputPort customerInputPort;

    private final CustomerOutputPort customerOutputPort;

    private final UserOutputPort userOutputPort;

    @PostMapping
    public ResponseEntity<CustomerResponse> create(@RequestBody @Valid CustomerRequest request) {

        var response = Optional.ofNullable(request)
                .map(dto -> customerInputPort.create(dto, customerOutputPort, userOutputPort))
                .map(CustomerPresenter::toCustomerResponse)
                .orElseThrow();

        return ResponseEntity
                .created(URI.create(URI_CUSTOMERS + "/" + response.id()))
                .body(response);
    }
}

package backend.finance.api_users.infrastructure.controllers;

import backend.finance.api_users.application_business_rules.dtos.output.CustomerResponse;
import backend.finance.api_users.application_business_rules.exception.http404.CustomerNotFoundCustomException;
import backend.finance.api_users.application_business_rules.usecases.CustomerCreateUseCase;
import backend.finance.api_users.application_business_rules.usecases.CustomerDisableUseCase;
import backend.finance.api_users.interface_adapters.controllers.CustomerController;
import backend.finance.api_users.interface_adapters.repositories.CustomerRepository;
import backend.finance.api_users.utils.BaseIntegrationTest;
import backend.finance.api_users.utils.CustomerTestFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CustomerControllerTest extends BaseIntegrationTest {

    @Autowired
    private CustomerController customerController;

    @Autowired
    private CustomerCreateUseCase customerCreateUseCase;

    @Autowired
    private CustomerDisableUseCase customerDeleteUseCase;

    @Autowired
    private CustomerRepository customerRepository;

    private CustomerResponse defaultCustomer;

    @BeforeEach
    void setUp() {
        var request = CustomerTestFactory.defaultRequest();

        defaultCustomer = customerCreateUseCase.create(request);
    }

    @Nested
    @DisplayName("FindById - casos válidos")
    class FindByIdValid {

        @Test
        @DisplayName("Deve consultar cliente por id.")
        void shouldFindByIdCustomer() {

            var response = customerController.findById(defaultCustomer.id()).getBody();

            assertNotNull(response);
            assertEquals(defaultCustomer.name(), response.name());
            assertEquals(defaultCustomer.email(), response.email());
            assertEquals(defaultCustomer.user().username(), response.user().username());
        }
    }

    @Nested
    @DisplayName("FindById - casos inválidos")
    class FindByIdInvalid {

        @Test
        @DisplayName("Deve lançar exceção ao consultar por id inexistente.")
        void shouldThrowOnNotFoundCustomer() {
            assertThrows(CustomerNotFoundCustomException.class, () -> customerController.findById(UUID.randomUUID()));
        }

        @Test
        @DisplayName("Deve lançar exceção ao consultar por id desativado, mas estar no banco de dados.")
        void shouldThrowOnNotFoundCustomerWithDisableId() {
            var idCustomer = defaultCustomer.id();

            var customerBuscadoAntes = customerRepository.findById(idCustomer).orElseThrow();
            assertNotNull(customerBuscadoAntes);
            assertTrue(customerBuscadoAntes.isActive());
            customerDeleteUseCase.disableById(idCustomer);

            assertThrows(CustomerNotFoundCustomException.class, () -> customerController.findById(idCustomer));

            var customerBuscadoDepois = customerRepository.findById(idCustomer).orElseThrow();
            assertNotNull(customerBuscadoDepois);
            assertFalse(customerBuscadoDepois.isActive());
        }
    }
}
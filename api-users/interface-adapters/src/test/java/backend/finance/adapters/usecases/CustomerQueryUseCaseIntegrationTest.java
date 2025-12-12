package backend.finance.adapters.usecases;

import backend.finance.adapters.repositories.CustomerRepository;
import backend.finance.adapters.utils.KafkaAvroIntegrationTest;
import backend.finance.adapters.utils.CustomerTestFactory;
import backend.finance.application.exceptions.http404.CustomerNotFoundCustomException;
import backend.finance.application.usecases.CustomerCreateUseCase;
import backend.finance.application.usecases.CustomerDisableUseCase;
import backend.finance.application.usecases.CustomerQueryUseCase;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@Tag("Customer")
@Tag("UseCase")
@Tag("Find")
@Tag("Query")
@Tag("Integration")
public class CustomerQueryUseCaseIntegrationTest extends KafkaAvroIntegrationTest {

    @Autowired
    private CustomerQueryUseCase customerQueryUseCase;

    @Autowired
    private CustomerCreateUseCase customerCreateUseCase;

    @Autowired
    private CustomerDisableUseCase customerDisableUseCase;

    @Autowired
    private CustomerRepository customerRepository;

    @Nested
    @Order(1)
    @DisplayName("FindById - casos inválidos")
    @Tag("FindByIdInvalid")
    class FindByIdInvalid {

        @Test
        @DisplayName("Deve lançar exceção ao consultar por id inexistente.")
        void deveLancarExcecaoPorIdNaoEncontrado() {
            assertThrows(CustomerNotFoundCustomException.class, () -> customerQueryUseCase
                    .findActiveById(UUID.randomUUID()));
        }

        @Test
        @DisplayName("Deve lançar exceção ao consultar por id desativado, mas manter no banco de dados.")
        void deveLancarExcecaoQuandoConsultarIdDesativado() {
            var request = CustomerTestFactory.defaultRequest();
            var defaultCustomer = customerCreateUseCase.create(request);
            var idCustomer = defaultCustomer.id();

            var customerBuscadoAntes = customerRepository.findById(idCustomer).orElseThrow();
            assertNotNull(customerBuscadoAntes);
            assertTrue(customerBuscadoAntes.isActive());
            customerDisableUseCase.disableById(idCustomer);

            assertThrows(CustomerNotFoundCustomException.class, () -> customerQueryUseCase
                    .findActiveById(idCustomer));

            var customerBuscadoDepois = customerRepository.findById(idCustomer).orElseThrow();
            assertNotNull(customerBuscadoDepois);
            assertFalse(customerBuscadoDepois.isActive());
        }
    }
}

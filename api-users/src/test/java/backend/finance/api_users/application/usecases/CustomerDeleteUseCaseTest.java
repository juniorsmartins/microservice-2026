package backend.finance.api_users.application.usecases;

import backend.finance.api_users.application_business_rules.dtos.output.CustomerResponse;
import backend.finance.api_users.application_business_rules.exception.http404.CustomerNotFoundCustomException;
import backend.finance.api_users.application_business_rules.usecases.CustomerCreateUseCase;
import backend.finance.api_users.application_business_rules.usecases.CustomerDisableUseCase;
import backend.finance.api_users.enterprise_business_rules.enums.RoleEnum;
import backend.finance.api_users.interface_adapters.repositories.CustomerRepository;
import backend.finance.api_users.utils.BaseIntegrationTest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static backend.finance.api_users.utils.CustomerTestFactory.buildRequest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CustomerDeleteUseCaseTest extends BaseIntegrationTest {

    private static final String EMAIL_TESTE = "teste@email.com";

    private static final String USERNAME_TESTE = "username-teste";

    @Autowired
    private CustomerCreateUseCase customerCreateUseCase;

    @Autowired
    private CustomerDisableUseCase customerDeleteUseCase;

    @Autowired
    private CustomerRepository customerRepository;

    private CustomerResponse defaultCustomer;

    @BeforeEach
    void setUp() {
        var request = buildRequest(USERNAME_TESTE, "password123",
                RoleEnum.ROLE_CUSTOMER.getValue(), "Maria Antonieta", EMAIL_TESTE);
        defaultCustomer = customerCreateUseCase.create(request);
    }

    @AfterEach
    void tearDown() {
        customerRepository.deleteAll();
    }

    @Nested
    @DisplayName("Delete - casos válidos")
    class DeleteValid {

        @Test
        @DisplayName("Deve desativar cliente, alterando o campo active para false, no banco de dados.")
        void shouldDisableCustomerInDatabaseWithFieldActiveFalse() {
            var idCustomer = defaultCustomer.id();

            var customerActiveTrue = customerRepository.findById(idCustomer);
            assertTrue(customerActiveTrue.isPresent());
            assertTrue(customerActiveTrue.get().isActive());
            assertTrue(customerActiveTrue.get().getUser().isActive());

            customerDeleteUseCase.disableById(idCustomer);

            var customerActiveFalse = customerRepository.findById(idCustomer);
            assertTrue(customerActiveFalse.isPresent());
            assertFalse(customerActiveFalse.get().isActive());
            assertFalse(customerActiveFalse.get().getUser().isActive());
        }
    }

    @Nested
    @DisplayName("Delete - casos inválidos")
    class DeleteInvalid {

        @Test
        @DisplayName("Deve lançar exceção ao desabilitar com ID inexistente.")
        void shouldThrowOnNotFoundNonexistentId() {
            assertThrows(CustomerNotFoundCustomException.class, () ->
                    customerDeleteUseCase.disableById(UUID.randomUUID()));
        }

        @Test
        @DisplayName("Deve lançar exceção ao desabilitar com ID existente, mas já desabilitado.")
        void dadaRequisicaoComIdDesativado_quandoDeleteById_entaoLancarExceptionAndTerNoBancoComoFalse() {
            var idCustomer = defaultCustomer.id();

            var customerBuscadoAntes = customerRepository.findById(idCustomer).orElseThrow();
            assertNotNull(customerBuscadoAntes);
            assertTrue(customerBuscadoAntes.isActive());
            assertTrue(customerBuscadoAntes.getUser().isActive());
            customerDeleteUseCase.disableById(idCustomer);

            assertThrows(CustomerNotFoundCustomException.class, () -> customerDeleteUseCase.disableById(idCustomer));

            var customerBuscadoDepois = customerRepository.findById(idCustomer).orElseThrow();
            assertNotNull(customerBuscadoDepois);
            assertFalse(customerBuscadoDepois.isActive());
            assertFalse(customerBuscadoDepois.getUser().isActive());
        }
    }
}
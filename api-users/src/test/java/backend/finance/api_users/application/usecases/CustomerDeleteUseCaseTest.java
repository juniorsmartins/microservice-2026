package backend.finance.api_users.application.usecases;

import backend.finance.api_users.application.configs.exception.http404.CustomerNotFoundCustomException;
import backend.finance.api_users.domain.entities.Customer;
import backend.finance.api_users.domain.enums.RoleEnum;
import backend.finance.api_users.infrastructure.repositories.CustomerRepository;
import backend.finance.api_users.utils.BaseIntegrationTest;
import backend.finance.api_users.utils.CustomerUtils;
import backend.finance.api_users.utils.UserUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CustomerDeleteUseCaseTest extends BaseIntegrationTest {

    private static final String EMAIL_TESTE = "teste@email.com";

    private static final String USERNAME_TESTE = "username-teste";

    @Autowired
    private CustomerCreateUseCase customerCreateUseCase;

    @Autowired
    private CustomerDeleteUseCase customerDeleteUseCase;

    @Autowired
    private CustomerRepository customerRepository;

    private Customer defaultCustomer;

    @BeforeEach
    void setUp() {
        var userRequest = UserUtils
                .trainRequest(USERNAME_TESTE, "password123", RoleEnum.ROLE_CUSTOMER.getValue());
        var request = CustomerUtils.trainRequest("Anne Frank", EMAIL_TESTE, userRequest);
        defaultCustomer = customerCreateUseCase.create(request);
    }

    @AfterEach
    void tearDown() {
        customerRepository.deleteAll();
    }

    @Nested
    @DisplayName("DeleteValid")
    class DeleteValid {

        @Test
        void dadaRequisicaoValida_quandoDeleteById_entaoArmazenarAtivoFalseNoBancoDeDados() {
            var idCustomer = defaultCustomer.getId();

            var customerActiveTrue = customerRepository.findById(idCustomer);
            assertTrue(customerActiveTrue.isPresent());
            assertTrue(customerActiveTrue.get().isActive());

            customerDeleteUseCase.disableById(idCustomer);

            var customerActiveFalse = customerRepository.findById(idCustomer).orElseThrow();
            assertFalse(customerActiveFalse.isActive());
            assertFalse(customerActiveFalse.getUser().isActive());
        }
    }

    @Nested
    @DisplayName("DeleteInvalid")
    class DeleteInvalid {

        @Test
        void dadaRequisicaoComIdInexistente_quandoDeleteById_entaoLancarCustomerNotFoundCustomException() {
            assertThrows(CustomerNotFoundCustomException.class, () -> customerDeleteUseCase.disableById(UUID.randomUUID()));
        }

        @Test
        void dadaRequisicaoComIdDesativado_quandoDeleteById_entaoLancarExceptionAndTerNoBancoComoFalse() {
            var idCustomer = defaultCustomer.getId();

            var customerBuscadoAntes = customerRepository.findById(idCustomer).orElseThrow();
            assertNotNull(customerBuscadoAntes);
            assertTrue(customerBuscadoAntes.isActive());
            customerDeleteUseCase.disableById(idCustomer);

            assertThrows(CustomerNotFoundCustomException.class, () -> customerDeleteUseCase.disableById(idCustomer));

            var customerBuscadoDepois = customerRepository.findById(idCustomer).orElseThrow();
            assertNotNull(customerBuscadoDepois);
            assertFalse(customerBuscadoDepois.isActive());
        }
    }
}
package backend.finance.api_user.infrastructure.controllers;

import backend.finance.api_user.application.configs.exception.http404.CustomerNotFoundCustomException;
import backend.finance.api_user.application.usecases.CustomerCreateUseCase;
import backend.finance.api_user.application.usecases.CustomerDeleteUseCase;
import backend.finance.api_user.domain.entities.Customer;
import backend.finance.api_user.domain.enums.RoleEnum;
import backend.finance.api_user.infrastructure.repositories.CustomerRepository;
import backend.finance.api_user.utils.BaseIntegrationTest;
import backend.finance.api_user.utils.CustomerUtils;
import backend.finance.api_user.utils.UserUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CustomerControllerTest extends BaseIntegrationTest {

    private static final String EMAIL_TESTE = "teste@email.com";

    private static final String USERNAME_TESTE = "username-teste";

    @Autowired
    private CustomerController customerController;

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
    @DisplayName("FindByIdValid")
    class FindByIdValid {

        @Test
        void dadaRequisicaoValida_quandoChamarFindById_entaoRetornarCustomer() {

            var response = customerController.findById(defaultCustomer.getId()).getBody();

            assertNotNull(response);
            assertEquals(defaultCustomer.getName(), response.name());
            assertEquals(defaultCustomer.getEmail(), response.email());
            assertEquals(defaultCustomer.getUser().getUsername(), response.user().username());
        }
    }

    @Nested
    @DisplayName("FindByIdInvalid")
    class FindByIdInvalid {

        @Test
        void dadaRequisicaoComIdInexistente_quandoChamarFindById_entaoLancarCustomerNotFoundCustomException() {
            assertThrows(CustomerNotFoundCustomException.class, () ->
                    customerController.findById(UUID.randomUUID()));
        }

        @Test
        void dadaRequisicaoComIdDesativado_quandoConsultarPorId_entaoLancarExcecao() {
            var idCustomer = defaultCustomer.getId();

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
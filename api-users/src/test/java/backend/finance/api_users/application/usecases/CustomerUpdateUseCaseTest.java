package backend.finance.api_users.application.usecases;

import backend.finance.api_users.application.configs.exception.http404.CustomerNotFoundCustomException;
import backend.finance.api_users.application.configs.exception.http409.EmailConflictRulesCustomException;
import backend.finance.api_users.application.configs.exception.http409.UsernameConflictRulesCustomException;
import backend.finance.api_users.application.dtos.input.CustomerRequest;
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
class CustomerUpdateUseCaseTest extends BaseIntegrationTest {

    private static final String EMAIL_TESTE = "teste@email.com";

    private static final String USERNAME_TESTE = "username-teste";

    @Autowired
    private CustomerCreateUseCase customerCreateUseCase;

    @Autowired
    private CustomerUpdateUseCase customerUpdateUseCase;

    @Autowired
    private CustomerRepository customerRepository;

    private Customer defaultCustomer;

    @BeforeEach
    void setUp() {
        var request =
                buildCustomerRequest(USERNAME_TESTE, "password123", RoleEnum.ROLE_CUSTOMER.getValue(),
                        "Anne Frank", EMAIL_TESTE);
        defaultCustomer = customerCreateUseCase.create(request);
    }

    @AfterEach
    void tearDown() {
        customerRepository.deleteAll();
    }

    @Nested
    @DisplayName("UpdateValid")
    class UpdateValid {

        @Test
        void dadaRequisicaoValida_quandoChamarUpdate_entaoSalvarNoBanco() {
            var idCustomer = defaultCustomer.getId();

            var request =
                    buildCustomerRequest("username999", "password999", RoleEnum.ROLE_ADMIN.getValue(),
                            "John Atual Doe", "doe@yahoo.com");

            customerUpdateUseCase.update(idCustomer, request);
            var customerDoBanco = customerRepository.findById(idCustomer).orElseThrow();

            assertEquals(request.name(), customerDoBanco.getName());
            assertEquals(request.email(), customerDoBanco.getEmail());
            assertTrue(customerDoBanco.isActive());
            assertEquals(request.user().username(), customerDoBanco.getUser().getUsername());
            assertEquals(request.user().password(), customerDoBanco.getUser().getPassword());
            assertTrue(customerDoBanco.getUser().isActive());
            assertEquals(request.user().role(), customerDoBanco.getUser().getRole().getName().getValue());
        }

        @Test
        void dadaRequisicaoValidaSemAlterarEmailAndUsername_quandoChamarUpdate_entaoSalvarNoBanco() {
            var idCustomer = defaultCustomer.getId();

            var request =
                    buildCustomerRequest(USERNAME_TESTE, "atual123", RoleEnum.ROLE_ADMIN.getValue(),
                            "John Atual Doe", EMAIL_TESTE);

            customerUpdateUseCase.update(idCustomer, request);
            var customerDoBanco = customerRepository.findById(idCustomer).orElseThrow();

            assertEquals(request.name(), customerDoBanco.getName());
            assertEquals(EMAIL_TESTE, customerDoBanco.getEmail());
            assertTrue(customerDoBanco.isActive());
            assertEquals(USERNAME_TESTE, customerDoBanco.getUser().getUsername());
            assertEquals(request.user().password(), customerDoBanco.getUser().getPassword());
            assertTrue(customerDoBanco.getUser().isActive());
            assertEquals(request.user().role(), customerDoBanco.getUser().getRole().getName().getValue());
        }
    }

    @Nested
    @DisplayName("UpdateInvalid")
    class UpdateInvalid {

        @Test
        void dadaRequisicaoInvalidaComIdInexistente_quandoChamarUpdate_entaoLancarCustomerNotFoundCustomException() {
            var idCustomerInvalid = UUID.randomUUID();

            var request =
                    buildCustomerRequest("robert_plant", "password123", RoleEnum.ROLE_CUSTOMER.getValue(),
                            "Robert Plant", "plant@gmail.com");

            assertThrows(CustomerNotFoundCustomException.class, () -> customerUpdateUseCase.update(idCustomerInvalid, request));
        }

        @Test
        void dadaRequisicaoInvalidaComEmailDuplicado_quandoChamarUpdate_entaoLancarEmailConflictRulesCustomException() {
            var emailDuplicado = "johnnis@uol.com";
            var requestCreate =
                    buildCustomerRequest("johndoe", "password123", RoleEnum.ROLE_CUSTOMER.getValue(),
                            "John Doe", emailDuplicado);
            customerCreateUseCase.create(requestCreate);

            var idCustomer = defaultCustomer.getId();
            var requestUpdate =
                    buildCustomerRequest("anne_frank_atual", "password888", RoleEnum.ROLE_ADMIN.getValue(),
                            "Anne Atual Frank", emailDuplicado);

            assertThrows(EmailConflictRulesCustomException.class, () ->
                    customerUpdateUseCase.update(idCustomer, requestUpdate));
        }

        @Test
        void dadaRequisicaoInvalidaComUsernameDuplicado_quandoChamarUpdate_entaoLancarUsernameConflictRulesCustomException() {
            var usernameDuplicate = "johndoe";
            var requestCreate =
                    buildCustomerRequest(usernameDuplicate, "password123", RoleEnum.ROLE_CUSTOMER.getValue(),
                            "John Doe", "doe@gmail.com");
            customerCreateUseCase.create(requestCreate);

            var idCustomer = defaultCustomer.getId();
            var requestUpdate =
                    buildCustomerRequest(usernameDuplicate, "password888", RoleEnum.ROLE_ADMIN.getValue(),
                            "Anne Atual Frank", "frank_atual@gmail.com");

            assertThrows(UsernameConflictRulesCustomException.class, () ->
                    customerUpdateUseCase.update(idCustomer, requestUpdate));
        }
    }

    private CustomerRequest buildCustomerRequest(String username, String password, String role, String name, String email) {
        var userRequest = UserUtils.trainRequest(username, password, role);
        return CustomerUtils.trainRequest(name, email, userRequest);
    }
}
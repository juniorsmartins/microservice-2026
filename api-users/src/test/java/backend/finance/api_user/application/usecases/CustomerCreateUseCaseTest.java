package backend.finance.api_user.application.usecases;

import backend.finance.api_user.application.configs.exception.http404.RoleNotFoundCustomException;
import backend.finance.api_user.application.configs.exception.http409.EmailConflictRulesCustomException;
import backend.finance.api_user.application.configs.exception.http409.UsernameConflictRulesCustomException;
import backend.finance.api_user.application.dtos.input.CustomerRequest;
import backend.finance.api_user.domain.enums.RoleEnum;
import backend.finance.api_user.infrastructure.repositories.CustomerRepository;
import backend.finance.api_user.utils.BaseIntegrationTest;
import backend.finance.api_user.utils.CustomerUtils;
import backend.finance.api_user.utils.UserUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CustomerCreateUseCaseTest extends BaseIntegrationTest {

    private static final String EMAIL_TESTE = "teste@email.com";

    private static final String USERNAME_TESTE = "username-teste";

    @Autowired
    private CustomerCreateUseCase customerCreateUseCase;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        var request =
                buildCustomerRequest(USERNAME_TESTE, "password123", RoleEnum.ROLE_CUSTOMER.getValue(),
                        "Anne Frank", EMAIL_TESTE);
        customerCreateUseCase.create(request);
    }

    @AfterEach
    void tearDown() {
        customerRepository.deleteAll();
    }

    @Nested
    @DisplayName("CreateValid")
    class CreateValid {

        @Test
        void dadaRequisicaoValida_quandoChamarCreate_entaoDeveSalvarNoBanco() {
            var request =
                    buildCustomerRequest("robertcm", "password123", RoleEnum.ROLE_CUSTOMER.getValue(),
                            "Robert C. Martin", "robert@gmail.com");

            var customer = customerCreateUseCase.create(request);
            var customerJpa = customerRepository.findById(customer.getId()).orElseThrow();

            assertEquals(request.name(), customerJpa.getName());
            assertEquals(request.email(), customerJpa.getEmail());
            assertTrue(customerJpa.isActive());
            assertEquals(request.user().username(), customerJpa.getUser().getUsername());
            assertEquals(request.user().password(), customerJpa.getUser().getPassword());
            assertTrue(customerJpa.getUser().isActive());
            assertEquals(RoleEnum.ROLE_CUSTOMER, customerJpa.getUser().getRole().getName());
        }

        @Test
        void dadaRequisicaoValidaComRoleAdmin_quandoChamarCreate_entaoDeveSalvarCustomerNoBanco() {
            var request =
                    buildCustomerRequest("robertcm", "password123", RoleEnum.ROLE_ADMIN.getValue(),
                            "Robert C. Martin", "robert@gmail.com");

            var customer = customerCreateUseCase.create(request);
            var customerJpa = customerRepository.findById(customer.getId()).orElseThrow();

            assertEquals(RoleEnum.ROLE_ADMIN, customerJpa.getUser().getRole().getName());
        }
    }

    @Nested
    @DisplayName("CreateInvalid")
    class CreateInvalid {

        @Test
        void dadaRequisicaoComEmailDuplicado_quandoChamarCreate_entaoDeveLancarEmailConflictRulesCustomException() {
            var request =
                    buildCustomerRequest("jeffbeck", "password123", RoleEnum.ROLE_ADMIN.getValue(),
                            "Jeff Beck", EMAIL_TESTE);

            assertThrows(EmailConflictRulesCustomException.class, () -> customerCreateUseCase.create(request));
        }

        @Test
        void dadaRequisicaoComUsernameDuplicado_quandoChamarCreate_entaoDeveLancarUsernameConflictRulesCustomException() {
            var request =
                    buildCustomerRequest(USERNAME_TESTE, "password123", RoleEnum.ROLE_CUSTOMER.getValue(),
                            "Jeff Beck", "beck@gmail.com");

            assertThrows(UsernameConflictRulesCustomException.class, () -> customerCreateUseCase.create(request));
        }

        @Test
        void dadaRequisicaoComRoleInvalid_quandoChamarCreate_entaoDeveLancarRoleNotFoundCustomException() {
            var request =
                    buildCustomerRequest("beck123", "password123", "ROLE_INVALID",
                            "Jeff Beck", "beck@gmail.com");

            assertThrows(RoleNotFoundCustomException.class, () -> customerCreateUseCase.create(request));
        }
    }

    private CustomerRequest buildCustomerRequest(String username, String password, String role, String name, String email) {
        var userRequest = UserUtils.trainRequest(username, password, role);
        return CustomerUtils.trainRequest(name, email, userRequest);
    }
}
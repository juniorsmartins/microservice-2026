package backend.finance.api_users.application.usecases;

import backend.finance.api_users.application_business_rules.exception.http404.RoleNotFoundCustomException;
import backend.finance.api_users.application_business_rules.exception.http409.EmailConflictRulesCustomException;
import backend.finance.api_users.application_business_rules.exception.http409.UsernameConflictRulesCustomException;
import backend.finance.api_users.application_business_rules.usecases.CustomerCreateUseCase;
import backend.finance.api_users.enterprise_business_rules.enums.RoleEnum;
import backend.finance.api_users.interface_adapters.repositories.CustomerRepository;
import backend.finance.api_users.utils.BaseIntegrationTest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static backend.finance.api_users.utils.CustomerTestFactory.buildRequest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CustomerCreateUseCaseTest extends BaseIntegrationTest {

    @Autowired
    private CustomerCreateUseCase customerCreateUseCase;

    @Autowired
    private CustomerRepository customerRepository;

    @Nested
    @DisplayName("Create - casos válidos")
    class CreateValid {

        @Test
        @DisplayName("Deve criar cliente com role Customer e salvar no banco.")
        void shouldCreateCustomerWithCustomerRole() {
            var request = buildRequest("robertcm", "password123",
                    RoleEnum.ROLE_CUSTOMER.getValue(), "Robert C. Martin", "robert@gmail.com");

            var created = customerCreateUseCase.create(request);

            var saved = customerRepository.findById(created.getId()).orElseThrow();
            assertAll("Cliente salvo corretamente.",
                    () -> assertEquals(request.name(), saved.getName()),
                    () -> assertEquals(request.email(), saved.getEmail()),
                    () -> assertTrue(saved.isActive()),
                    () -> assertEquals(request.user().username(), saved.getUser().getUsername()),
                    () -> assertEquals(request.user().password(), saved.getUser().getPassword()),
                    () -> assertTrue(saved.getUser().isActive()),
                    () -> assertEquals(RoleEnum.ROLE_CUSTOMER, saved.getUser().getRole().getName())
            );
        }

        @Test
        @DisplayName("Deve criar cliente com role Admin e salvar no banco.")
        void shouldCreateCustomerWithAdminRole() {
            var request = buildRequest("admin", "pass123",
                    RoleEnum.ROLE_ADMIN.getValue(), "Admin User", "admin@example.com");

            var created = customerCreateUseCase.create(request);

            var saved = customerRepository.findById(created.getId()).orElseThrow();
            assertEquals(RoleEnum.ROLE_ADMIN, saved.getUser().getRole().getName());
        }
    }

    @Nested
    @DisplayName("Create - casos inválidos")
    class CreateInvalid {

        private static final String EMAIL_DEFAULT = "teste@email.com";

        private static final String USERNAME_DEFAULT = "username-teste";

        @BeforeEach
        void setUp() {
            var request = buildRequest(USERNAME_DEFAULT, "pass",
                    RoleEnum.ROLE_CUSTOMER.getValue(), "Existente", EMAIL_DEFAULT);

            customerCreateUseCase.create(request);
        }

        @Test
        @DisplayName("Deve lançar exceção ao criar com email duplicado.")
        void shouldThrowOnDuplicateEmail() {
            var request = buildRequest("jeffbeck", "password123",
                    RoleEnum.ROLE_ADMIN.getValue(), "Jeff Beck", EMAIL_DEFAULT);

            assertThrows(EmailConflictRulesCustomException.class, () -> customerCreateUseCase.create(request));
        }

        @Test
        @DisplayName("Deve lançar exceção ao criar com username duplicado.")
        void shouldThrowOnDuplicateUsername() {
            var request = buildRequest(USERNAME_DEFAULT, "password123",
                    RoleEnum.ROLE_CUSTOMER.getValue(), "Jeff Beck", "beck@gmail.com");

            assertThrows(UsernameConflictRulesCustomException.class, () -> customerCreateUseCase.create(request));
        }

        @Test
        @DisplayName("Deve lançar exceção ao criar com permissão inexistente.")
        void shouldThrowOnInvalidRole() {
            var request = buildRequest("beck123", "password123", "ROLE_INVALID",
                            "Jeff Beck", "beck@gmail.com");

            assertThrows(RoleNotFoundCustomException.class, () -> customerCreateUseCase.create(request));
        }
    }
}
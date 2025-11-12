package backend.finance.adapters.usecases;

import backend.finance.adapters.repositories.CustomerRepository;
import backend.finance.adapters.utils.BaseIntegrationTest;
import backend.finance.adapters.utils.CustomerTestFactory;
import backend.finance.application.exceptions.http404.RoleNotFoundCustomException;
import backend.finance.application.exceptions.http409.EmailConflictRulesCustomException;
import backend.finance.application.exceptions.http409.UsernameConflictRulesCustomException;
import backend.finance.application.usecases.CustomerCreateUseCase;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@Tag("Customer")
@Tag("UseCase")
@Tag("Create")
@Tag("Integration")
class CustomerCreateUseCaseIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private CustomerCreateUseCase customerCreateUseCase;

    @Autowired
    private CustomerRepository customerRepository;

    @Nested
    @Order(1)
    @DisplayName("Create - casos válidos")
    @Tag("CreateValid")
    class CreateValid {

        @Test
        @DisplayName("Deve criar cliente com role Customer e salvar no banco.")
        void shouldCreateCustomerWithCustomerRole() {
            var request = CustomerTestFactory
                    .buildRequest("robertcm", "password123", "ROLE_CUSTOMER",
                            "Robert C. Martin", "robert@gmail.com");

            var created = customerCreateUseCase.create(request);

            var saved = customerRepository.findById(created.id()).orElseThrow();
            assertAll("Cliente salvo corretamente.",
                    () -> assertEquals(request.name(), saved.getName()),
                    () -> assertEquals(request.email(), saved.getEmail()),
                    () -> assertTrue(saved.isActive()),
                    () -> assertEquals(request.user().username(), saved.getUser().getUsername()),
                    () -> assertEquals(request.user().password(), saved.getUser().getPassword()),
                    () -> assertTrue(saved.getUser().isActive()),
                    () -> assertEquals("ROLE_CUSTOMER", saved.getUser().getRole().getName())
            );
        }

        @Test
        @DisplayName("Deve criar cliente com role Admin e salvar no banco.")
        void shouldCreateCustomerWithAdminRole() {
            var request = CustomerTestFactory
                    .buildRequest("admin", "pass123", "ROLE_ADMIN", "Admin User",
                            "admin@example.com");

            var created = customerCreateUseCase.create(request);

            var saved = customerRepository.findById(created.id()).orElseThrow();
            assertEquals("ROLE_ADMIN", saved.getUser().getRole().getName());
        }
    }

    @Nested
    @Order(2)
    @DisplayName("Create - casos inválidos")
    @Tag("CreateInvalid")
    class CreateInvalid {

        private static final String EMAIL_DEFAULT = "teste@email.com";

        private static final String USERNAME_DEFAULT = "username-teste";

        @BeforeEach
        void setUp() {
            var request = CustomerTestFactory
                    .buildRequest(USERNAME_DEFAULT, "pass", "ROLE_CUSTOMER", "Existente", EMAIL_DEFAULT);

            customerCreateUseCase.create(request);
        }

        @Test
        @DisplayName("Deve lançar exceção ao criar com email duplicado.")
        void shouldThrowOnDuplicateEmail() {
            var request = CustomerTestFactory
                    .buildRequest("jeffbeck", "password123", "ROLE_ADMIN", "Jeff Beck",
                            EMAIL_DEFAULT);

            assertThrows(EmailConflictRulesCustomException.class, () -> customerCreateUseCase.create(request));
        }

        @Test
        @DisplayName("Deve lançar exceção ao criar com username duplicado.")
        void shouldThrowOnDuplicateUsername() {
            var request = CustomerTestFactory
                    .buildRequest(USERNAME_DEFAULT, "password123", "ROLE_CUSTOMER", "Jeff Beck",
                            "beck@gmail.com");

            assertThrows(UsernameConflictRulesCustomException.class, () -> customerCreateUseCase.create(request));
        }

        @Test
        @DisplayName("Deve lançar exceção ao criar com permissão inexistente.")
        void shouldThrowOnInvalidRole() {
            var request = CustomerTestFactory
                    .buildRequest("beck123", "password123", "ROLE_INVALID", "Jeff Beck",
                            "beck@gmail.com");

            assertThrows(RoleNotFoundCustomException.class, () -> customerCreateUseCase.create(request));
        }
    }
}
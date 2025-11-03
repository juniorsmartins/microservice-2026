package backend.finance.api_users.application.usecases;

import backend.finance.api_users.application_business_rules.dtos.output.CustomerResponse;
import backend.finance.api_users.application_business_rules.exception.http404.CustomerNotFoundCustomException;
import backend.finance.api_users.application_business_rules.exception.http409.EmailConflictRulesCustomException;
import backend.finance.api_users.application_business_rules.exception.http409.UsernameConflictRulesCustomException;
import backend.finance.api_users.application_business_rules.usecases.CustomerCreateUseCase;
import backend.finance.api_users.application_business_rules.usecases.CustomerUpdateUseCase;
import backend.finance.api_users.enterprise_business_rules.enums.RoleEnum;
import backend.finance.api_users.interface_adapters.repositories.CustomerRepository;
import backend.finance.api_users.utils.BaseIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static backend.finance.api_users.utils.CustomerTestFactory.buildRequest;
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

    private CustomerResponse defaultCustomer;

    @BeforeEach
    void setUp() {
        var request = buildRequest(USERNAME_TESTE, "password123",
                RoleEnum.ROLE_CUSTOMER.getValue(), "Anne Frank", EMAIL_TESTE);
        defaultCustomer = customerCreateUseCase.create(request);
    }

    @Nested
    @DisplayName("Update - casos válidos")
    class UpdateValid {

        @Test
        @DisplayName("Deve atualizar cliente e salvar no banco de dados.")
        void shouldUpdateCustomerInDatabase() {
            var request = buildRequest("username999", "password999",
                    RoleEnum.ROLE_ADMIN.getValue(), "John Atual Doe", "doe@yahoo.com");

            customerUpdateUseCase.update(defaultCustomer.id(), request);

            var saved = customerRepository.findById(defaultCustomer.id()).orElseThrow();
            assertAll(
                    () -> assertEquals(request.name(), saved.getName()),
                    () -> assertEquals(request.email(), saved.getEmail()),
                    () -> assertTrue(saved.isActive()),
                    () -> assertEquals(request.user().username(), saved.getUser().getUsername()),
                    () -> assertEquals(request.user().password(), saved.getUser().getPassword()),
                    () -> assertTrue(saved.getUser().isActive()),
                    () -> assertEquals(RoleEnum.ROLE_ADMIN, saved.getUser().getRole().getName())
            );
        }

        @Test
        @DisplayName("Deve atualizar cliente, manter email e username, e salvar no banco de dados.")
        void shouldUpdateCustomerNotEmailAndUsernameAndSavedInDatabase() {
            var request = buildRequest(USERNAME_TESTE, "atual123",
                    RoleEnum.ROLE_ADMIN.getValue(), "John Atual Doe", EMAIL_TESTE);

            customerUpdateUseCase.update(defaultCustomer.id(), request);

            var saved = customerRepository.findById(defaultCustomer.id()).orElseThrow();
            assertAll(
                    () -> assertEquals(request.name(), saved.getName()),
                    () -> assertEquals(EMAIL_TESTE, saved.getEmail()),
                    () -> assertTrue(saved.isActive()),
                    () -> assertEquals(USERNAME_TESTE, saved.getUser().getUsername()),
                    () -> assertEquals(request.user().password(), saved.getUser().getPassword()),
                    () -> assertTrue(saved.getUser().isActive()),
                    () -> assertEquals(RoleEnum.ROLE_ADMIN, saved.getUser().getRole().getName())
            );
        }
    }

    @Nested
    @DisplayName("Update - casos inválidos")
    class UpdateInvalid {

        private CustomerResponse customer;

        @BeforeEach
        void setUp() {
            var request = buildRequest("robert_plant", "password123",
                    RoleEnum.ROLE_CUSTOMER.getValue(), "Robert Plant", "plant@gmail.com");
            customer = customerCreateUseCase.create(request);
        }

        @Test
        @DisplayName("Deve retornar 404 com ID inexistente.")
        void shouldReturnNotFoundOnInvalidId() {
            var idCustomerInvalid = UUID.randomUUID();

            var request = buildRequest("robert_atual_plant", "password0980",
                    RoleEnum.ROLE_CUSTOMER.getValue(), "Robert Atual Plant", "plan_atualt@gmail.com");

            assertThrows(CustomerNotFoundCustomException.class, () ->
                    customerUpdateUseCase.update(idCustomerInvalid, request));
        }

        @Test
        @DisplayName("Deve retornar 409 com email duplicado.")
        void shouldReturnConflictOnDuplicateEmail() {
            var requestUpdate = buildRequest("anne_frank_atual", "password888",
                    RoleEnum.ROLE_ADMIN.getValue(), "Anne Atual Frank", EMAIL_TESTE);

            assertThrows(EmailConflictRulesCustomException.class, () ->
                    customerUpdateUseCase.update(customer.id(), requestUpdate));
        }

        @Test
        @DisplayName("Deve retornar 409 com username duplicado.")
        void shouldReturnConflictOnDuplicateUsername() {
            var requestUpdate = buildRequest(USERNAME_TESTE, "password888",
                    RoleEnum.ROLE_CUSTOMER.getValue(), "Anne Atual Frank", "emailqualquer@uol.com");

            assertThrows(UsernameConflictRulesCustomException.class, () ->
                    customerUpdateUseCase.update(customer.id(), requestUpdate));
        }
    }
}
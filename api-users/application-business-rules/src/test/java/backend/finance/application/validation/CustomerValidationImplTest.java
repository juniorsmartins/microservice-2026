package backend.finance.application.validation;

import backend.finance.application.exceptions.http409.EmailConflictRulesCustomException;
import backend.finance.application.ports.output.CustomerQueryOutputPort;
import backend.finance.enterprise.enums.RoleEnum;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static backend.finance.application.Utils.CustomerTestFactory.buildDto;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@Tag("Customer")
@Tag("Validation")
class CustomerValidationImplTest {

    @Mock
    private CustomerQueryOutputPort customerQueryOutputPort;

    @InjectMocks
    private CustomerValidationImpl customerValidation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customerValidation = new CustomerValidationImpl(customerQueryOutputPort);
    }

    @Nested
    @Order(1)
    @DisplayName("Validation - casos válidos.")
    @Tag("ValidationValid")
    class ValidationValid {

        @Test
        @DisplayName("Deve checar duplicação de email sem ID.")
        void devePassarQuandoNaoTemConflitoSemId() {
            var email = "novo@yahoo.com";
            when(customerQueryOutputPort.findByEmail(email)).thenReturn(Optional.empty());

            assertDoesNotThrow(() -> customerValidation.checkDuplicateEmail(null, email));

            verify(customerQueryOutputPort).findByEmail(email);
        }

        @Test
        @DisplayName("Deve checar duplicação de email com ID.")
        void devePassarQuandoNaoTemConflitoComId() {
            var email = "novo@yahoo.com";
            var customerDtoBanco = buildDto("username", "password",
                    RoleEnum.ROLE_CUSTOMER, "NomeX", email);

            when(customerQueryOutputPort.findByEmail(email)).thenReturn(Optional.of(customerDtoBanco));

            assertDoesNotThrow(() -> customerValidation.checkDuplicateEmail(customerDtoBanco.id(), email));

            verify(customerQueryOutputPort).findByEmail(email);
        }
    }

    @Nested
    @Order(2)
    @DisplayName("Validation - casos inválidos.")
    @Tag("ValidationInvalid")
    class ValidationInvalid {

        @Test
        @DisplayName("Deve lançar exceção quando encontrar email duplicado sem ID.")
        void deveLancarExcecaoQuandoConfirmarDuplicacaoDeEmailSemId() {
            var email = "duplicado@email.com";
            var customerDtoBanco = buildDto("username", "password",
                    RoleEnum.ROLE_CUSTOMER, "NomeX", email);

            when(customerQueryOutputPort.findByEmail(email)).thenReturn(Optional.of(customerDtoBanco));

            var excecao = assertThrows(EmailConflictRulesCustomException.class,
                    () -> customerValidation.checkDuplicateEmail(null, email));

            assertEquals(email, excecao.getValue());
            assertEquals("exception.resource.conflict.rules.email", excecao.getMessage());
            verify(customerQueryOutputPort, times(1)).findByEmail(email);
        }

        @Test
        @DisplayName("Deve lançar exceção quando encontrar email duplicado com ID.")
        void deveLancarExcecaoQuandoConfirmarDuplicacaoDeEmailComId() {
            var email = "duplicado@email.com";
            var customerDtoBanco = buildDto("username", "password",
                    RoleEnum.ROLE_CUSTOMER, "NomeX", email);

            when(customerQueryOutputPort.findByEmail(email))
                    .thenAnswer(i -> Optional.of(customerDtoBanco));

            var excecao = assertThrows(EmailConflictRulesCustomException.class,
                    () -> customerValidation.checkDuplicateEmail(UUID.randomUUID(), email));

            assertEquals(email, excecao.getValue());
            assertEquals("exception.resource.conflict.rules.email", excecao.getMessage());
            verify(customerQueryOutputPort, times(1)).findByEmail(email);
        }
    }
}
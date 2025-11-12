package backend.finance.application.validation;

import backend.finance.application.exceptions.http409.UsernameConflictRulesCustomException;
import backend.finance.application.ports.output.CustomerQueryOutputPort;
import backend.finance.enterprise.enums.RoleEnum;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static backend.finance.application.utils.CustomerTestFactory.buildDto;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@Tag("User")
@Tag("Validation")
class UserValidationImplTest {

    @Mock
    private CustomerQueryOutputPort customerQueryOutputPort;

    @InjectMocks
    private UserValidationImpl userValidation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userValidation = new UserValidationImpl(customerQueryOutputPort);
    }

    @Nested
    @Order(1)
    @DisplayName("Validation - casos válidos")
    @Tag("ValidationValid")
    class ValidationValid {

        @Test
        @DisplayName("Deve checar duplicação de username, sem Id, sem conflito.")
        void devePassarQuandoNaoPossuiConflitoPorEmailSemId() {
            var username = "username9898";
            when(customerQueryOutputPort.findByUsername(username)).thenReturn(Optional.empty());

            assertDoesNotThrow(() -> userValidation.checkDuplicateUsername(null, username));

            verify(customerQueryOutputPort, times(1)).findByUsername(username);
        }

        @Test
        @DisplayName("Deve checar duplicação de username, com Id, sem conflito.")
        void devePassarQuandoNaoPossuiConflitoPorEmailComId() {
            var username = "username9898";
            var customerDtoBanco = buildDto(username, "password",
                    RoleEnum.ROLE_CUSTOMER, "NomeX", "email@teste.com.br");

            when(customerQueryOutputPort.findByUsername(username)).thenReturn(Optional.of(customerDtoBanco));

            assertDoesNotThrow(() -> userValidation.checkDuplicateUsername(customerDtoBanco.id(), username));

            verify(customerQueryOutputPort, times(1)).findByUsername(username);
        }
    }

    @Nested
    @Order(2)
    @DisplayName("Validation - casos inválidos")
    @Tag("ValidationInvalid")
    class ValidationInvalid {

        @Test
        @DisplayName("Deve lançar exceção quando checar duplicação de username, sem Id, com conflito.")
        void deveLancarExcecaoQuandoPossuiConflitoPorEmailSemId() {
            var username = "username_duplicado";
            var customerDtoBanco = buildDto(username, "password",
                    RoleEnum.ROLE_CUSTOMER, "NomeX", "email@teste.com.br");

            when(customerQueryOutputPort.findByUsername(username)).thenReturn(Optional.of(customerDtoBanco));

            var excecao = assertThrows(UsernameConflictRulesCustomException.class,
                    () -> userValidation.checkDuplicateUsername(null, username));

            assertEquals("exception.resource.conflict.rules.username", excecao.getMessage());
            assertEquals(username, excecao.getValue());
            verify(customerQueryOutputPort, times(1)).findByUsername(username);
        }

        @Test
        @DisplayName("Deve lançar exceção quando checar duplicação de username, com Id, com conflito.")
        void deveLancarExcecaoQuandoPossuiConflitoPorEmailComId() {
            var username = "username_duplicado";
            var customerDtoBanco = buildDto(username, "password",
                    RoleEnum.ROLE_CUSTOMER, "NomeX", "email@teste.com.br");

            when(customerQueryOutputPort.findByUsername(username))
                    .thenAnswer(i -> Optional.of(customerDtoBanco));

            var excecao = assertThrows(UsernameConflictRulesCustomException.class,
                    () -> userValidation.checkDuplicateUsername(UUID.randomUUID(), username));

            assertEquals(username, excecao.getValue());
            assertEquals("exception.resource.conflict.rules.username", excecao.getMessage());
            verify(customerQueryOutputPort, times(1)).findByUsername(username);
        }
    }
}
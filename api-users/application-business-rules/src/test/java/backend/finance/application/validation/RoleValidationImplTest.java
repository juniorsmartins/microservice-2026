package backend.finance.application.validation;

import backend.finance.application.dtos.RoleDto;
import backend.finance.application.exceptions.http404.RoleNotFoundCustomException;
import backend.finance.application.ports.output.RoleOutputPort;
import backend.finance.enterprise.enums.RoleEnum;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@Tag("Role")
@Tag("Validation")
class RoleValidationImplTest {

    @Mock
    private RoleOutputPort roleOutputPort;

    @InjectMocks
    private RoleValidationImpl roleValidation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        roleValidation = new RoleValidationImpl(roleOutputPort);
    }

    @Nested
    @Order(1)
    @DisplayName("Validation - casos válidos")
    @Tag("ValidationValid")
    class ValidationValid {

        @Test
        @DisplayName("Deve encontrar role Customer.")
        void deveEncontrarRoleCustomer() {
            var roleName = RoleEnum.ROLE_CUSTOMER.name();
            var roleDto = new RoleDto(UUID.randomUUID(), roleName);
            when(roleOutputPort.findByName(roleName)).thenReturn(Optional.of(roleDto));

            assertDoesNotThrow(() -> roleValidation.getOrCreateRole(roleName));

            verify(roleOutputPort, times(1)).findByName(roleName);
        }
    }

    @Nested
    @Order(2)
    @DisplayName("Validation - casos inválidos")
    @Tag("ValidationInvalid")
    class ValidationInvalid {

        @Test
        @DisplayName("Deve lançar exceção quando não encontrar role.")
        void deveLancarExcecaoQuandoNaoEncontrarRole() {
            var roleName = "ROLE_INEXISTENTE";

            var excecao = assertThrows(RoleNotFoundCustomException.class,
                    () -> roleValidation.getOrCreateRole(roleName));

            assertEquals(roleName, excecao.getValue());
            assertEquals("exception.resource.not-found.role", excecao.getMessage());
            verifyNoInteractions(roleOutputPort);
        }
    }
}
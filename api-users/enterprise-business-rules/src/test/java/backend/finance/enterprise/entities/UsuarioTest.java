package backend.finance.enterprise.entities;

import backend.finance.enterprise.exceptions.AllNullFieldsCustomException;
import backend.finance.enterprise.exceptions.AttributeExceededMaximumLimitException;
import backend.finance.enterprise.exceptions.BadRequestCustomException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@Tag("Usuário")
class UsuarioTest {

    @Mock
    private Permissao permissao;

    @Nested
    @Order(1)
    @DisplayName("UsuarioValido")
    @Tag("CreateValid")
    class CreateValid {

        @Test
        @DisplayName("Deve criar usuário padrão.")
        void deveCriarUsuario() {
            // Arrange
            var username = "username123";
            var password = "password123";
            // Act
            var usuario = Usuario.create(null, username, password, permissao, true);
            // Assert
            assertEquals(username, usuario.getUsername());
            assertEquals(password, usuario.getPassword());
        }

        @Test
        @DisplayName("Deve desativar usuário alterando active para false.")
        void deveDesativarUsuario() {
            var usuario = Usuario
                    .create(null, "username123", "password123", permissao, true);

            assertTrue(usuario.isActive());
            usuario.disable();
            assertFalse(usuario.isActive());
        }
    }

    @Nested
    @Order(2)
    @DisplayName("UsernameInvalid")
    @Tag("UsernameInvalid")
    class UsernameInvalid {

        @Test
        @DisplayName("Deve lançar exceção quando criar usuário com username nulo.")
        void deveLancarExcecaoQuandoCriarComUsernameNulo() {
            var exception = assertThrows(AllNullFieldsCustomException.class,
                    () -> Usuario.create(null, null, "password123", permissao, true));
            assertEquals("exception.field.null.request.all", exception.getMessage());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "   "})
        @DisplayName("Deve lançar exceção quando criar usuário com username vazio ou em branco.")
        void deveLancarExcecaoQuandoCriarComUsernameVazioOuEmBranco(String username) {
            var exception = assertThrows(AllNullFieldsCustomException.class,
                    () -> Usuario.create(null, username, "password123", permissao, true));
            assertEquals("exception.field.null.request.all", exception.getMessage());
        }

        @Test
        @DisplayName("Deve lançar exceção quando criar usuário com username maior.")
        void deveLancarExcecaoQuandoCriarComUsernameMaior() {
            var username = "abcdefghijlmnoperstuvxz123123123123123123123123123123123";
            var exception = assertThrows(AttributeExceededMaximumLimitException.class,
                    () -> Usuario.create(null, username, "password123", permissao, true));
            assertEquals("exception.field.exceeded-maximum-limit.request", exception.getMessage());
        }

        @ParameterizedTest
        @ValueSource(strings = {"   ", "", "abcdefghijlmnoperstuvxz123123123123123123123123123123123"})
        @DisplayName("Deve lançar exceção quando setar username inválido.")
        void deveLancarExcecaoQuandoSetarUsernameInvalido(String username) {
            Set<String> mensagensEsperadas = Set.of(
                    "exception.field.null.request.all",
                    "exception.field.exceeded-maximum-limit.request"
            );

            var usuario = Usuario
                    .create(null,"username123", "password123", permissao, true);

            var exception = assertThrows(BadRequestCustomException.class,
                    () -> usuario.setUsername(username));

            var mensagemReal = exception.getMessage();
            assertTrue(mensagensEsperadas.contains(mensagemReal));
        }
    }

    @Nested
    @Order(4)
    @DisplayName("PasswordInvalido")
    @Tag("PasswordInvalido")
    class PasswordInvalido {

        @Test
        @DisplayName("Deve lançar exceção quando criar usuário com password nulo.")
        void deveLancarExcecaoQuandoCriarComPasswordNulo() {
            var exception = assertThrows(AllNullFieldsCustomException.class,
                    () -> Usuario.create(null, "username123", null, permissao, true));
            assertEquals("exception.field.null.request.all", exception.getMessage());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "   "})
        @DisplayName("Deve lançar exceção quando criar usuário com password vazio ou em branco.")
        void deveLancarExcecaoQuandoCriarComPasswordVazioOuEmBranco(String password) {
            var exception = assertThrows(AllNullFieldsCustomException.class,
                    () -> Usuario.create(null, "username123", password, permissao, true));
            assertEquals("exception.field.null.request.all", exception.getMessage());
        }

        @Test
        @DisplayName("Deve lançar exceção quando criar usuário com password maior.")
        void deveLancarExcecaoQuandoCriarComPasswordMaior() {
            var password = "abcdefghijlmnoperstuvxz123123123123123123123123123123123qreqreerwerqwerwqerwerwerwewerwerwer32145678abcdefghijlmnoperstuvxz123123123123123123123123123123123qreqreerwerqwerwqerwerwerwewerwerwer321456783";
            var exception = assertThrows(AttributeExceededMaximumLimitException.class,
                    () -> Usuario.create(null, "username123", password, permissao, true));
            assertEquals("exception.field.exceeded-maximum-limit.request", exception.getMessage());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "   ", "abcdefghijlmnoperstuvxz123123123123123123123123123123123qreqreerwerqwerwqerwerwerwewerwerwer32145678abcdefghijlmnoperstuvxz123123123123123123123123123123123qreqreerwerqwerwqerwerwerwewerwerwer321456783"})
        @DisplayName("Deve lançar exceção quando setar password inválido.")
        void deveLancarExcecaoQuandoSetarComPasswordInvalido(String password) {
            Set<String> mensagensEsperadas = Set.of(
                    "exception.field.null.request.all",
                    "exception.field.exceeded-maximum-limit.request"
            );

            var usuario = Usuario
                    .create(null,"username123", "password123", permissao, true);

            var exception = assertThrows(BadRequestCustomException.class,
                    () -> usuario.setPassword(password));

            var mensagemReal = exception.getMessage();
            assertTrue(mensagensEsperadas.contains(mensagemReal));
        }
    }
}
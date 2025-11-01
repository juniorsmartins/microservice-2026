package backend.finance.api_users.domain.entities;

import backend.finance.api_users.application_business_rules.exception.http400.AllNullFieldsCustomException;
import backend.finance.api_users.application_business_rules.exception.http400.AttributeExceededMaximumLimitException;
import backend.finance.api_users.enterprise_business_rules.entities.Permissao;
import backend.finance.api_users.enterprise_business_rules.entities.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UsuarioTest {

    @Mock
    private Permissao permissao;

    @Nested
    @DisplayName("UsuarioValido")
    class UsuarioValido {

        @Test
        void dadoValoresValidos_quandoCreateUsuario_entaoCriarComSucesso() {
            // Arrange
            var username = "username123";
            var password = "password123";
            // Act
            var usuario = Usuario.create(null, username, password, permissao, true);
            // Assert
            assertEquals(username, usuario.getUsername());
            assertEquals(password, usuario.getPassword());
        }
    }

    @Nested
    @DisplayName("AtributoUsernameInvalido")
    class AtributoUsernameInvalido {

        @Test
        void dadoUsernameNulo_quandoCreateUsuario_entaoLancarException() {
            var exception = assertThrows(AllNullFieldsCustomException.class,
                    () -> Usuario.create(null, null, "password123", permissao, true));
            assertEquals("exception.field.null.request.all", exception.getMessage());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "   "})
        void dadoUsernameVazioOuEmBranco_quandoCreateUsuario_entaoLancarException(String username) {
            var exception = assertThrows(AllNullFieldsCustomException.class,
                    () -> Usuario.create(null, username, "password123", permissao, true));
            assertEquals("exception.field.null.request.all", exception.getMessage());
        }

        @Test
        void dadoUsernameMaior_quandoCreateUsuario_entaoLancarException() {
            var username = "abcdefghijlmnoperstuvxz123123123123123123123123123123123";
            var exception = assertThrows(AttributeExceededMaximumLimitException.class,
                    () -> Usuario.create(null, username, "password123", permissao, true));
            assertEquals("exception.field.exceeded-maximum-limit.request", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("AtributoPasswordInvalido")
    class AtributoPasswordInvalido {

        @Test
        void dadoPasswordNulo_quandoCreateUsuario_entaoLancarException() {
            var exception = assertThrows(AllNullFieldsCustomException.class,
                    () -> Usuario.create(null, "username123", null, permissao, true));
            assertEquals("exception.field.null.request.all", exception.getMessage());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "   "})
        void dadoPasswordVazioOuEmBranco_quandoCreateUsuario_entaoLancarException(String password) {
            var exception = assertThrows(AllNullFieldsCustomException.class,
                    () -> Usuario.create(null, "username123", password, permissao, true));
            assertEquals("exception.field.null.request.all", exception.getMessage());
        }

        @Test
        void dadoPasswordMaior_quandoCreateUsuario_entaoLancarException() {
            var password = "abcdefghijlmnoperstuvxz123123123123123123123123123123123qreqreerwerqwerwqerwerwerwewerwerwer32145678abcdefghijlmnoperstuvxz123123123123123123123123123123123qreqreerwerqwerwqerwerwerwewerwerwer321456783";
            var exception = assertThrows(AttributeExceededMaximumLimitException.class,
                    () -> Usuario.create(null, "username123", password, permissao, true));
            assertEquals("exception.field.exceeded-maximum-limit.request", exception.getMessage());
        }
    }
}
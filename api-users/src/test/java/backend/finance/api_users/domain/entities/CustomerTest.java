package backend.finance.api_users.domain.entities;

import backend.finance.api_users.application.configs.exception.http400.AllNullFieldsCustomException;
import backend.finance.api_users.application.configs.exception.http400.AttributeExceededMaximumLimitException;
import backend.finance.api_users.application.configs.exception.http400.EmailInvalidFormatCustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CustomerTest {

    @Mock
    private Usuario usuarioMock;

    @Nested
    @DisplayName("CustomerValid")
    class CustomerValid {

        @Test
        void dadoValoresValidos_quandoCreateCustomer_entaoCriarComSucesso() {
            // Arrange
            var id = UUID.randomUUID();
            var name = "Donald Trump";
            var email = "trump@gmail.com";
            // Act
            var customer = Customer.create(id, name, email, usuarioMock, true);
            // Assert
            assertEquals(id, customer.getId());
            assertEquals(name, customer.getName());
            assertEquals(email, customer.getEmail());
        }
    }

    @Nested
    @DisplayName("AtributoNameInvalid")
    class AtributoNameInvalid {

        @Test
        void dadoNameNull_quandoCreateCustomer_entaoLancarException() {
            // Arrange
            var id = UUID.randomUUID();
            var email = "trump@gmail.com";
            // Act & Assert
            var exception = assertThrows(AllNullFieldsCustomException.class,
                    () -> Customer.create(id, null, email, usuarioMock, true));
            assertEquals("exception.field.null.request.all", exception.getMessage());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "   "})
        void dadoNameVazioOuEmBranco_quandoCreateCustomer_entaoLancarException(String name) {
            // Arrange
            var id = UUID.randomUUID();
            var email = "trump@gmail.com";
            // Act & Assert
            var exception = assertThrows(AllNullFieldsCustomException.class,
                    () -> Customer.create(id, name, email, usuarioMock, true));
            assertEquals("exception.field.null.request.all", exception.getMessage());
        }

        @Test
        void dadoNameMaior_quandoCreateCustomer_entaoLancarException() {
            var id = UUID.randomUUID();
            var name = "Donald Trump da Silva Sauro da Manchuria Azul do Mar";
            var email = "trump@gmail.com";
            var exception = assertThrows(AttributeExceededMaximumLimitException.class,
                    () -> Customer.create(id, name, email, usuarioMock, true));
            assertEquals("exception.field.exceeded-maximum-limit.request", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("AtributoEmailInvalid")
    class AtributoEmailInvalid {

        @Test
        void dadoEmailNull_quandoCreateCustomer_entaoLancarException() {
            var id = UUID.randomUUID();
            var name = "Donald Trump";
            var exception = assertThrows(AllNullFieldsCustomException.class,
                    () -> Customer.create(id, name, null, usuarioMock, true));
            assertEquals("exception.field.null.request.all", exception.getMessage());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "   "})
        void dadoEmailVazioOuEmBrando_quandoCreateCustomer_entaoLancarException(String email) {
            var id = UUID.randomUUID();
            var name = "Donald Trump";
            var exception = assertThrows(AllNullFieldsCustomException.class,
                    () -> Customer.create(id, name, email, usuarioMock, true));
            assertEquals("exception.field.null.request.all", exception.getMessage());
        }

        @ParameterizedTest
        @ValueSource(strings = {"email_teste_sem_arroba.com", "teste@", "123232!!!@%$#", "@yahoo.com"})
        void dadoEmailNoFormatoIncorreto_quandoCreateCustomer_entaoLancarException(String email) {
            var id = UUID.randomUUID();
            var name = "Donald Trump";
            var exception = assertThrows(EmailInvalidFormatCustomException.class,
                    () -> Customer.create(id, name, email, usuarioMock, true));
            assertEquals("exception.poorly.formulated.request.email", exception.getMessage());
        }
    }
}
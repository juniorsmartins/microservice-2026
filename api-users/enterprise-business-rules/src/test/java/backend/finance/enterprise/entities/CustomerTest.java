package backend.finance.enterprise.entities;

import backend.finance.enterprise.exceptions.AllNullFieldsCustomException;
import backend.finance.enterprise.exceptions.AttributeExceededMaximumLimitException;
import backend.finance.enterprise.exceptions.BadRequestCustomException;
import backend.finance.enterprise.exceptions.EmailInvalidFormatCustomException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@Tag("Customer")
class CustomerTest {

    @Mock
    private Usuario usuarioMock;

    @Nested
    @Order(1)
    @DisplayName("CustomerValid")
    @Tag("CustomerValid")
    class CustomerValid {

        @Test
        @DisplayName("Deve criar customer padrão.")
        void deveCriarCustomer() {
            // Arrange
            var id = UUID.randomUUID();
            var name = "Donald Trump";
            var email = "trump@gmail.com";
            // Act
            var customer = new Customer(id, name, email, usuarioMock, true);
            // Assert
            assertEquals(id, customer.getId());
            assertEquals(name, customer.getName());
            assertEquals(email, customer.getEmail());
        }

        @Test
        @DisplayName("Deve desativar customer alterando active para false.")
        void deveDesativarCustomer() {
            var customer = new Customer(null, "Vladimir Putin", "vlad@email.com", usuarioMock, true);

            assertTrue(customer.isActive());
            customer.disable();
            assertFalse(customer.isActive());
        }
    }

    @Nested
    @Order(2)
    @DisplayName("NameInvalid")
    @Tag("NameInvalid")
    class NameInvalid {

        @Test
        @DisplayName("Deve lançar exceção quando criar customer com nome nulo.")
        void deveLancarExcecaoQuandoCriarComNomeNulo() {
            // Arrange
            var id = UUID.randomUUID();
            var email = "trump@gmail.com";
            // Act & Assert
            var exception = assertThrows(AllNullFieldsCustomException.class,
                    () -> new Customer(id, null, email, usuarioMock, true));
            assertEquals("exception.field.null.request.all", exception.getMessage());
        }

        @ParameterizedTest
        @ValueSource(strings = {"   ", ""})
        @DisplayName("Deve lançar exceção quando criar customer com nome vazio ou em branco.")
        void deveLancarExcecaoQuandoCriarComNomeVazioOuEmBranco(String name) {
            // Arrange
            var id = UUID.randomUUID();
            var email = "trump@gmail.com";
            // Act & Assert
            var exception = assertThrows(AllNullFieldsCustomException.class,
                    () -> new Customer(id, name, email, usuarioMock, true));
            assertEquals("exception.field.null.request.all", exception.getMessage());
        }

        @Test
        @DisplayName("Deve lançar exceção quando criar customer com nome maior.")
        void deveLancarExcecaoQuandoCriarComNomeMaior() {
            var id = UUID.randomUUID();
            var name = "Donald Trump da Silva Sauro da Manchuria Azul do Mar";
            var email = "trump@gmail.com";
            var exception = assertThrows(AttributeExceededMaximumLimitException.class,
                    () -> new Customer(id, name, email, usuarioMock, true));
            assertEquals("exception.field.exceeded-maximum-limit.request", exception.getMessage());
        }

        @ParameterizedTest
        @ValueSource(strings = {"   ", "", "Donald Trump da Silva Sauro da Manchuria Azul do Mar"})
        @DisplayName("Deve lançar exceção quando setar nome inválido.")
        void deveLancarExcecaoQuandoSetarNomeInvalido(String nome) {
            Set<String> mensagensEsperadas = Set.of(
                    "exception.field.null.request.all",
                    "exception.field.exceeded-maximum-limit.request"
            );

            var customer = new Customer(null, "Vladimir Putin", "vlad@email.com", usuarioMock, true);

            var exception = assertThrows(BadRequestCustomException.class,
                    () -> customer.setName(nome));

            var mensagemReal = exception.getMessage();
            assertTrue(mensagensEsperadas.contains(mensagemReal));
        }
    }

    @Nested
    @Order(3)
    @DisplayName("EmailInvalid")
    @Tag("EmailInvalid")
    class EmailInvalid {

        @Test
        @DisplayName("Deve lançar exceção quando criar com email nulo.")
        void deveLancarExcecaoQuandoCriarComEmailNulo() {
            var id = UUID.randomUUID();
            var name = "Donald Trump";
            var exception = assertThrows(AllNullFieldsCustomException.class,
                    () -> new Customer(id, name, null, usuarioMock, true));
            assertEquals("exception.field.null.request.all", exception.getMessage());
        }

        @ParameterizedTest
        @ValueSource(strings = {"   ", ""})
        @DisplayName("Deve lançar exceção quando criar customer com email vazio ou em branco.")
        void deveLancarExcecaoQuandoCriarComEmailVazioOuEmBrando(String email) {
            var id = UUID.randomUUID();
            var name = "Donald Trump";
            var exception = assertThrows(AllNullFieldsCustomException.class,
                    () -> new Customer(id, name, email, usuarioMock, true));
            assertEquals("exception.field.null.request.all", exception.getMessage());
        }

        @ParameterizedTest
        @ValueSource(strings = {"email_teste_sem_arroba.com", "teste@", "123232!!!@%$#", "@yahoo.com"})
        @DisplayName("Deve lançar exceção quando criar customer com email em formato inválido.")
        void deveLancarExcecaoQuandoCriarComEmailEmFormatoInvalido(String email) {
            var id = UUID.randomUUID();
            var name = "Donald Trump";
            var exception = assertThrows(EmailInvalidFormatCustomException.class,
                    () -> new Customer(id, name, email, usuarioMock, true));
            assertEquals("exception.poorly.formulated.request.email", exception.getMessage());
        }

        @ParameterizedTest
        @ValueSource(strings = {"   ", "", "email_teste_sem_arroba.com", "teste@", "@yahoo.com"})
        @DisplayName("Deve lançar exceção quando setar email inválido.")
        void deveLancarExcecaoQuandoSetarEmailInvalido(String email) {
            Set<String> mensagensEsperadas = Set.of(
                    "exception.field.null.request.all",
                    "exception.poorly.formulated.request.email"
            );

            var customer = new Customer(null, "Vladimir Putin", "vlad@email.com", usuarioMock, true);

            var exception = assertThrows(BadRequestCustomException.class,
                    () -> customer.setEmail(email));
            String mensagemReal = exception.getMessage();

            assertTrue(mensagensEsperadas.contains(mensagemReal));
        }
    }
}
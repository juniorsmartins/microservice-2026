package backend.finance.adapters.controllers;

import backend.finance.adapters.utils.BaseIntegrationTest;
import backend.finance.adapters.utils.CustomerTestFactory;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static backend.finance.adapters.utils.CustomerTestFactory.buildRequest;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@Tag("Customer")
@Tag("Controller")
@Tag("Create")
@Tag("Integration")
class CustomerControllerCreateIntegrationTest extends BaseIntegrationTest {

    private static final String URI_CUSTOMER = "/v1/customers";

    private static final String EMAIL_TESTE = "teste@email.com";

    private static final String USERNAME_TESTE = "username-teste";

    @LocalServerPort
    private int randomPort;

    @Autowired
    private CustomerController customerController;

    @BeforeEach
    void setUp() {
        RestAssured.port = randomPort; // Configura a porta dinâmica
        RestAssured.basePath = URI_CUSTOMER;

        var request = buildRequest(USERNAME_TESTE, "password123",
                "ROLE_CUSTOMER", "Anne Frank", EMAIL_TESTE);
        customerController.create(request);
    }

    @Nested
    @Order(1)
    @DisplayName("Create - casos válidos.")
    @Tag("CreateValid")
    class CreateValid {

        @Test
        @DisplayName("Deve criar cliente via POST e retornar 201.")
        void deveCriarCustomer() {
            var request = CustomerTestFactory.defaultRequest();

            RestAssured.given()
                        .contentType(ContentType.JSON)
                        .body(request)
                    .when()
                        .post()
                    .then()
                        .statusCode(HttpStatus.CREATED.value())
                        .body("id", notNullValue())
                        .body("name", equalTo(request.name()))
                        .body("email", equalTo(request.email()))
                        .body("active", equalTo(true))
                        .body("user.id", notNullValue())
                        .body("user.username", equalTo(request.user().username()))
                        .body("user.active", equalTo(true));
        }
    }

    @Nested
    @Order(2)
    @DisplayName("Create - casos inválidos.")
    @Tag("CreateInvalid")
    class CreateInvalid {

        @Test
        @DisplayName("Deve retornar 409 com email duplicado.")
        void shouldReturnConflictOnDuplicateEmail() {
            var request = buildRequest("jeffbeck", "password123",
                    "ROLE_ADMIN", "Jeff Beck", EMAIL_TESTE);

            RestAssured.given()
                        .contentType(ContentType.JSON)
                        .body(request)
                    .when()
                        .post()
                    .then()
                        .statusCode(HttpStatus.CONFLICT.value())
                        .body("title", equalTo("Esse email já existe no sistema: " + EMAIL_TESTE + "."));
        }

        @Test
        @DisplayName("Deve retornar 409 com username duplicado.")
        void shouldReturnConflictOnDuplicateUsername() {
            var request = buildRequest(USERNAME_TESTE, "password123",
                    "ROLE_CUSTOMER", "Jeff Beck", "jbeck@email.com");

            RestAssured.given()
                        .contentType(ContentType.JSON)
                        .body(request)
                    .when()
                        .post()
                    .then()
                        .statusCode(HttpStatus.CONFLICT.value())
                        .body("title", equalTo("Esse username já existe no sistema: " + USERNAME_TESTE + "."));
        }

        @Test
        @DisplayName("Deve retornar 404 com role inválida.")
        void shouldReturnNotFoundOnInvalidRole() {
            var request = buildRequest("jbeck123", "password123", "ROLE_INVALID",
                    "Jeff Beck", "jbeck@email.com");

            RestAssured.given()
                        .contentType(ContentType.JSON)
                        .body(request)
                    .when()
                        .post()
                    .then()
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .body("title", equalTo("Permissão não encontrada pelo nome: ROLE_INVALID."));
        }

        @Test
        @DisplayName("Deve retornar 400 com nome de tamanho inválido.")
        void shouldReturnBadRequestOnNameSizeInvalid() {
            var request = buildRequest("jeffbeck10", "password123", "ROLE_ADMIN",
                    "Jeff Beck10 Jeff Beck Jeff Beck Jeff Beck Jeff Beck", "js@email.com");

            RestAssured.given()
                        .contentType(ContentType.JSON)
                        .body(request)
                    .when()
                        .post()
                    .then()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .body("title", equalTo("O atributo name ultrapassou o limite máximo de " + 50 + " caracteres."));
        }

        @Test
        @DisplayName("Deve retornar 400 com email em formato inválido.")
        void shouldReturnBadRequestOnEmailInvalidFormat() {
            var request = buildRequest("jeffsuther10", "password0101",
                    "ROLE_ADMIN", "Jeff Sutherland10",
                    "jeffsutherlandjeffsutherlandjeffsutherlandjeffsutherlandjeffsutherlandjeffsutheremail.com");

            RestAssured.given()
                        .contentType(ContentType.JSON)
                        .body(request)
                    .when()
                        .post()
                    .then()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .body("title", equalTo("Email com formato inválido: " + request.email() + "."));
        }

        @Test
        @DisplayName("Deve retornar 400 com username de tamanho inválido.")
        void shouldReturnBadRequestOnUsernameInvalidSize() {
            var request = buildRequest("_jeffsuther10_jeffsuther10_jeffsuther10_jeffsuther10_",
                    "password0101", "ROLE_ADMIN", "Jeff Sutherland10", "jeffsutherland@email.com");

            RestAssured.given()
                        .contentType(ContentType.JSON)
                        .body(request)
                    .when()
                        .post()
                    .then()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .body("title", equalTo("O atributo username ultrapassou o limite máximo de " + 50 + " caracteres."));
        }

        @Test
        @DisplayName("Deve retornar 400 com password de tamanho inválido.")
        void shouldReturnBadRequestOnPasswordInvalidSize() {
            var request = buildRequest("jeffsuther10_",
                    "_password0101_password0101_password0101_password0101_password0101", "ROLE_ADMIN",
                    "Jeff Sutherland10", "jeffsutherland@email.com");

            RestAssured.given()
                        .contentType(ContentType.JSON)
                        .body(request)
                    .when()
                        .post()
                    .then()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .body("title", equalTo("O atributo password ultrapassou o limite máximo de " + 55 + " caracteres."));
        }
    }
}
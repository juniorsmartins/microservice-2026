package backend.finance.adapters.controllers;

import backend.finance.adapters.utils.CustomerTestFactory;
import backend.finance.adapters.utils.KafkaAvroIntegrationTest;
import backend.finance.application.dtos.response.CustomerResponse;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static backend.finance.adapters.utils.CustomerTestFactory.buildRequest;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@Tag("Customer")
@Tag("Controller")
@Tag("Update")
@Tag("Integration")
class CustomerControllerUpdateIntegrationTest extends KafkaAvroIntegrationTest {

    private static final String URI_CUSTOMER = "/api-users/v1/customers";

    private static final String EMAIL_TESTE = "teste@email.com";

    private static final String USERNAME_TESTE = "username-teste";

    @LocalServerPort
    private int randomPort;

    @Autowired
    private CustomerController customerController;

    private CustomerResponse defaultCustomerResponse;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = randomPort; // Configura a porta dinâmica
        RestAssured.basePath = URI_CUSTOMER;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        var request = buildRequest(USERNAME_TESTE, "password123",
                "ROLE_CUSTOMER", "Anne Frank", EMAIL_TESTE);
        defaultCustomerResponse = customerController.create(request).getBody();
    }

    @Nested
    @Order(1)
    @DisplayName("Update - casos válidos.")
    @Tag("UpdateValid")
    class UpdateValid {

        @Test
        @DisplayName("Deve atualizar cliente via PUT e retornar 200.")
        void deveAtualizarCustomer() {
            var idCustomer = defaultCustomerResponse.id();

            var request = buildRequest("anne_frank_atual", "password123",
                    "ROLE_ADMIN", "Anne Atual Frank", "frank_atual@gmail.com");

            RestAssured.given()
                        .contentType(ContentType.JSON)
                        .body(request)
                    .when()
                        .put("/{id}", idCustomer)
                    .then()
                        .statusCode(HttpStatus.OK.value())
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
    @DisplayName("Update - casos inválidos.")
    @Tag("UpdateInvalid")
    class UpdateInvalid {

        private CustomerResponse customerResponse;

        @BeforeEach
        void setUp() {
            var requestCreate = buildRequest("johndoe", "password123",
                    "ROLE_CUSTOMER", "John Doe", "doe@gmail.com");
            customerResponse = customerController.create(requestCreate).getBody();
        }

        @Test
        @DisplayName("Deve retornar 404 com ID inexistente.")
        void deveLancarExcecaoNotFoundPorIdNaoEncontrado() {
            var idCustomerInvalid = UUID.randomUUID();

            var request = buildRequest("robert_plant", "password123",
                    "ROLE_CUSTOMER", "Robert Plant", "plant@gmail.com");

            RestAssured.given()
                        .contentType(ContentType.JSON)
                        .body(request)
                    .when()
                        .put("/{id}", idCustomerInvalid)
                    .then()
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .body("title", equalTo("Cliente não encontrado por id: " + idCustomerInvalid + "."));
        }

        @Test
        @DisplayName("Deve retornar 409 com email duplicado.")
        void deveLancarExcecaoPorConflitoDeEmailDuplicado() {
            var requestUpdate = buildRequest("johnatualdoe", "password55544",
                    "ROLE_ADMIN", "John Atual Doe", EMAIL_TESTE);

            RestAssured.given()
                        .contentType(ContentType.JSON)
                        .body(requestUpdate)
                    .when()
                        .put("/{id}", customerResponse.id())
                    .then()
                        .statusCode(HttpStatus.CONFLICT.value())
                        .body("title", equalTo("Esse email já existe no sistema: " + EMAIL_TESTE + "."));
        }

        @Test
        @DisplayName("Deve retornar 409 com username duplicado.")
        void deveLancarExcecaoPorConflitoDeUsernameDuplicado() {
            var requestUpdate = buildRequest(USERNAME_TESTE, "password3421", "ROLE_ADMIN",
                    "Jeff Sutherland Filho", "jsuther@gmail.com");

            RestAssured.given()
                        .contentType(ContentType.JSON)
                        .body(requestUpdate)
                    .when()
                        .put("/{id}", customerResponse.id())
                    .then()
                        .statusCode(HttpStatus.CONFLICT.value())
                        .body("title", equalTo("Esse username já existe no sistema: " + USERNAME_TESTE + "."));
        }

        @Test
        @DisplayName("Deve lançar exceção ao consultar customer desativado.")
        void deveLancarExcecaoQuandoConsultarCustomerDesativado() {
            var request = CustomerTestFactory.defaultRequest();
            var id = customerResponse.id();
            assertTrue(customerResponse.active());

            customerController.disableById(id);
            var customerDoBanco = customerRepository.findById(id).orElseThrow();
            assertFalse(customerDoBanco.isActive());

            RestAssured.given()
                        .contentType(ContentType.JSON)
                        .body(request)
                    .when()
                        .put("/{id}", id)
                    .then()
                        .statusCode(HttpStatus.NOT_FOUND.value())
                    .body("title", equalTo("Cliente não encontrado por id: " + id + "."));
        }
    }
}
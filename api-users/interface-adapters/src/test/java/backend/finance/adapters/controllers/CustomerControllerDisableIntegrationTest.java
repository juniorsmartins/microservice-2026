package backend.finance.adapters.controllers;

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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@Tag("Customer")
@Tag("Controller")
@Tag("Disable")
@Tag("Integration")
class CustomerControllerDisableIntegrationTest extends KafkaAvroIntegrationTest {

    private static final String URI_CUSTOMER = "/v1/customers";

    private static final String EMAIL_TESTE = "teste@email.com";

    private static final String USERNAME_TESTE = "username-teste";

    @LocalServerPort
    private int randomPort;

    @Autowired
    private CustomerController customerController;

    private CustomerResponse defaultCustomerResponse;

    @BeforeEach
    void setUp() {
        RestAssured.port = randomPort; // Configura a porta dinâmica
        RestAssured.basePath = URI_CUSTOMER;

        var request = buildRequest(USERNAME_TESTE, "password123",
                "ROLE_CUSTOMER", "Anne Frank", EMAIL_TESTE);
        defaultCustomerResponse = customerController.create(request).getBody();
    }

    @Test
    void testando() {
        assert defaultCustomerResponse != null : "CustomerResponse is null!";
        assert defaultCustomerResponse.id() != null : "Customer ID is null!";
    }

    @Nested
    @Order(1)
    @DisplayName("Disable - casos válidos.")
    @Tag("DisableValid")
    class DisableValid {

        @Test
        @DisplayName("Deve desativar cliente via DELETE e retornar 200.")
        void shouldDeleteCustomer() {

            RestAssured.given()
                        .contentType(ContentType.JSON)
                    .when()
                        .delete("/{id}", defaultCustomerResponse.id())
                    .then()
                        .statusCode(HttpStatus.NO_CONTENT.value());
        }
    }

    @Nested
    @Order(2)
    @DisplayName("Disable - casos inválidos.")
    @Tag("DisableInvalid")
    class DisableInvalid {

        @Test
        @DisplayName("Deve retornar 404 com ID não encontrado.")
        void shouldReturnNotFoundOnInvalidId() {
            var idInexistente = UUID.randomUUID();

            RestAssured.given()
                        .contentType(ContentType.JSON)
                    .when()
                        .delete("/{id}", idInexistente)
                    .then()
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .body("title", equalTo("Cliente não encontrado por id: " + idInexistente + "."));
        }
    }
}
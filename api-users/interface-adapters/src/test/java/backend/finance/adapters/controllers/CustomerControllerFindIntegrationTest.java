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
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@Tag("Customer")
@Tag("Controller")
@Tag("Find")
@Tag("Query")
@Tag("Integration")
class CustomerControllerFindIntegrationTest extends KafkaAvroIntegrationTest {

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

    @Nested
    @Order(1)
    @DisplayName("FindById - casos válidos.")
    @Tag("FindByIdValid")
    class FindByIdValid {

        @Test
        @DisplayName("Deve consultar cliente via GET e retornar 200.")
        void shouldFindByIdCustomer() {

            RestAssured.given()
                        .contentType(ContentType.JSON)
                    .when()
                        .get("/{id}", defaultCustomerResponse.id())
                    .then()
                        .statusCode(HttpStatus.OK.value())
                        .body("id", notNullValue())
                        .body("name", equalTo(defaultCustomerResponse.name()))
                        .body("email", equalTo(defaultCustomerResponse.email()))
                        .body("active", equalTo(true))
                        .body("user.id", notNullValue())
                        .body("user.username", equalTo(defaultCustomerResponse.user().username()))
                        .body("user.active", equalTo(true));
        }
    }

    @Nested
    @Order(2)
    @DisplayName("FindById - casos inválidos.")
    @Tag("FindByIdInvalid")
    class FindByIdInvalid {

        @Test
        @DisplayName("Deve retornar 404 com ID não encontrado.")
        void shouldReturnNotFoundOnInvalidId() {
            var idNotFound = UUID.randomUUID();

            RestAssured.given()
                        .contentType(ContentType.JSON)
                    .when()
                        .get("/{id}", idNotFound)
                    .then()
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .body("title", equalTo("Cliente não encontrado por id: " + idNotFound + "."));
        }
    }
}
package backend.finance.api_users.infrastructure.controllers;

import backend.finance.api_users.application_business_rules.dtos.output.CustomerResponse;
import backend.finance.api_users.enterprise_business_rules.enums.RoleEnum;
import backend.finance.api_users.interface_adapters.controllers.CustomerController;
import backend.finance.api_users.utils.BaseIntegrationTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static backend.finance.api_users.enterprise_business_rules.constants.ConstantsValidation.*;
import static backend.finance.api_users.utils.CustomerTestFactory.buildRequest;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerControllerRestAssuredTest extends BaseIntegrationTest {

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
                RoleEnum.ROLE_CUSTOMER.getValue(), "Anne Frank", EMAIL_TESTE);

        defaultCustomerResponse = customerController.create(request).getBody();
    }

    @Nested
    @DisplayName("Create - casos válidos")
    class CreateValid {

        @Test
        @DisplayName("Deve criar cliente com sucesso via POST.")
        void shouldCreateCustomer() {
            var request = buildRequest("johndoe", "password123",
                    RoleEnum.ROLE_CUSTOMER.getValue(), "John Doe", "doe@gmail.com");

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
    @DisplayName("Create - casos inválidos")
    class CreateInvalid {

        @Test
        @DisplayName("Deve retornar 409 com email duplicado.")
        void shouldReturnConflictOnDuplicateEmail() {
            var request = buildRequest("jeffbeck", "password123",
                    RoleEnum.ROLE_ADMIN.getValue(), "Jeff Beck", EMAIL_TESTE);

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
                    RoleEnum.ROLE_CUSTOMER.getValue(), "Jeff Beck", "jbeck@email.com");

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
            var request = buildRequest("jeffbeck10", "password123",
                    RoleEnum.ROLE_ADMIN.getValue(), "Jeff Beck10 Jeff Beck Jeff Beck Jeff Beck Jeff Beck",
                    "js@email.com");

            RestAssured.given()
                        .contentType(ContentType.JSON)
                        .body(request)
                    .when()
                        .post()
                    .then()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .body("title", equalTo("Há um ou mais campos inválidos."))
                        .body("fields.name[0]", equalTo("tamanho deve ser entre 0 e " + NAME_SIZE_MAX));
        }

        @Test
        @DisplayName("Deve retornar 400 com email em formato inválido.")
        void shouldReturnBadRequestOnEmailInvalidFormat() {
            var request = buildRequest("jeffsuther10", "password0101",
                    RoleEnum.ROLE_ADMIN.getValue(), "Jeff Sutherland10",
                    "jeffsutherlandjeffsutherlandjeffsutherlandjeffsutherlandjeffsutherlandjeffsuther@email.com");

            RestAssured.given()
                        .contentType(ContentType.JSON)
                        .body(request)
                    .when()
                        .post()
                    .then()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .body("title", equalTo("Há um ou mais campos inválidos."))
                        .body("fields.email[0]", equalTo("deve ser um endereço de e-mail bem formado"));
        }

        @Test
        @DisplayName("Deve retornar 400 com username de tamanho inválido.")
        void shouldReturnBadRequestOnUsernameInvalidSize() {
            var request = buildRequest("_jeffsuther10_jeffsuther10_jeffsuther10_jeffsuther10_",
                    "password0101", RoleEnum.ROLE_ADMIN.getValue(), "Jeff Sutherland10",
                    "jeffsutherland@email.com");

            RestAssured.given()
                        .contentType(ContentType.JSON)
                        .body(request)
                    .when()
                        .post()
                    .then()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .body("title", equalTo("Há um ou mais campos inválidos."))
                        .body("fields.'user.username'[0]", equalTo("tamanho deve ser entre 0 e " + USERNAME_SIZE_MAX));
        }

        @Test
        @DisplayName("Deve retornar 400 com password de tamanho inválido.")
        void shouldReturnBadRequestOnPasswordInvalidSize() {
            var request = buildRequest("jeffsuther10_",
                    "_password0101_password0101_password0101_password0101_password0101",
                    RoleEnum.ROLE_ADMIN.getValue(), "Jeff Sutherland10", "jeffsutherland@email.com");

            RestAssured.given()
                        .contentType(ContentType.JSON)
                        .body(request)
                    .when()
                        .post()
                    .then()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .body("title", equalTo("Há um ou mais campos inválidos."))
                        .body("fields.'user.password'[0]", equalTo("tamanho deve ser entre 0 e " + PASSWORD_SIZE_MAX));
        }
    }

    @Nested
    @DisplayName("Update - casos válidos")
    class UpdateValid {

        @Test
        @DisplayName("Deve atualizar cliente via PUT e retornar 200.")
        void shouldUpdateCustomer() {
            var idCustomer = defaultCustomerResponse.id();

            var request = buildRequest("anne_frank_atual", "password123",
                    RoleEnum.ROLE_ADMIN.getValue(), "Anne Atual Frank", "frank_atual@gmail.com");

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
    @DisplayName("Update - casos inválidos")
    class UpdateInvalid {

        private CustomerResponse customerResponse;

        @BeforeEach
        void setUp() {
            var requestCreate = buildRequest("johndoe", "password123",
                    RoleEnum.ROLE_CUSTOMER.getValue(), "John Doe", "doe@gmail.com");
            customerResponse = customerController.create(requestCreate).getBody();
        }

        @Test
        @DisplayName("Deve retornar 404 com ID inexistente.")
        void shouldReturnNotFoundOnInvalidId() {
            var idCustomerInvalid = UUID.randomUUID();

            var request = buildRequest("robert_plant", "password123",
                    RoleEnum.ROLE_CUSTOMER.getValue(), "Robert Plant", "plant@gmail.com");

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
        void shouldReturnConflictOnDuplicateEmail() {
            var requestUpdate = buildRequest("johnatualdoe", "password55544",
                    RoleEnum.ROLE_ADMIN.getValue(), "John Atual Doe", EMAIL_TESTE);

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
        void shouldReturnConflictOnDuplicateUsername() {
            var requestUpdate = buildRequest(USERNAME_TESTE, "password3421",
                    RoleEnum.ROLE_ADMIN.getValue(), "Jeff Sutherland Filho", "jsuther@gmail.com");

            RestAssured.given()
                        .contentType(ContentType.JSON)
                        .body(requestUpdate)
                    .when()
                        .put("/{id}", customerResponse.id())
                    .then()
                        .statusCode(HttpStatus.CONFLICT.value())
                        .body("title", equalTo("Esse username já existe no sistema: " + USERNAME_TESTE + "."));
        }
    }

    @Nested
    @DisplayName("Delete - casos válidos")
    class DeleteValid {

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
    @DisplayName("Delete - casos inválidos")
    class DeleteInvalid {

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

    @Nested
    @DisplayName("FindById - casos válidos")
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
    @DisplayName("FindById - casos inválidos")
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
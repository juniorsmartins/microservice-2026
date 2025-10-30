package backend.finance.api_users.infrastructure.controllers;

import backend.finance.api_users.application.dtos.output.CustomerResponse;
import backend.finance.api_users.domain.enums.RoleEnum;
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
        @DisplayName("Deve retornar 409 com username duplicado")
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
        @DisplayName("Deve retornar 404 com role inválida")
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
    }

    @Nested
    @DisplayName("Update - casos válidos")
    class UpdateValid {

        @Test
        void dadaRequisicaoValida_quandoChamarUpdate_entaoRetornarCustomerAtualizado() {
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
    @DisplayName("UpdateInvalid")
    class UpdateInvalid {

        @Test
        void dadaRequisicaoInvalidaComIdInexistente_quandoChamarUpdate_entaoLancarException() {
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
        void dadaRequisicaoInvalidaComEmailDuplicado_quandoChamarUpdate_entaoLancarException() {
            var emailDuplicate = "doe@gmail.com";
            var requestCreate = buildRequest("johndoe", "password123",
                    RoleEnum.ROLE_CUSTOMER.getValue(), "John Doe", emailDuplicate);

            customerController.create(requestCreate);

            var idCustomer = defaultCustomerResponse.id();
            var requestUpdate = buildRequest("anne_frank_atual", "password888",
                    RoleEnum.ROLE_ADMIN.getValue(), "Anne Atual Frank", emailDuplicate);

            RestAssured.given()
                        .contentType(ContentType.JSON)
                        .body(requestUpdate)
                    .when()
                        .put("/{id}", idCustomer)
                    .then()
                        .statusCode(HttpStatus.CONFLICT.value())
                        .body("title", equalTo("Esse email já existe no sistema: " + emailDuplicate + "."));
        }

        @Test
        void dadaRequisicaoInvalidaComUsernameDuplicado_quandoChamarUpdate_entaoLancarException() {
            var usernameDuplicate = "johndoe";
            var requestCreate = buildRequest(usernameDuplicate, "password123",
                    RoleEnum.ROLE_CUSTOMER.getValue(), "John Doe", "doe@gmail.com");

            customerController.create(requestCreate);

            var idCustomer = defaultCustomerResponse.id();
            var requestUpdate = buildRequest(usernameDuplicate, "password888",
                    RoleEnum.ROLE_ADMIN.getValue(), "Anne Atual Frank", "frank_atual@gmail.com");

            RestAssured.given()
                        .contentType(ContentType.JSON)
                        .body(requestUpdate)
                    .when()
                        .put("/{id}", idCustomer)
                    .then()
                        .statusCode(HttpStatus.CONFLICT.value())
                        .body("title", equalTo("Esse username já existe no sistema: " + usernameDuplicate + "."));
        }
    }

    @Nested
    @DisplayName("DeleteValid")
    class DeleteValid {

        @Test
        void dadaRequisicaoValida_quandoDeleteById_entaoRetornarSucesso() {

            RestAssured.given()
                        .contentType(ContentType.JSON)
                    .when()
                        .delete("/{id}", defaultCustomerResponse.id())
                    .then()
                        .statusCode(HttpStatus.NO_CONTENT.value());
        }
    }

    @Nested
    @DisplayName("DeleteInvalid")
    class DeleteInvalid {

        @Test
        void dadaRequisicaoComIdInexistente_quandoDeleteById_entaoLancarException() {
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
    @DisplayName("FindByIdValid")
    class FindByIdValid {

        @Test
        void dadaRequisicaoValida_quandoChamarFindById_entaoRetornarCustomer() {

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
    @DisplayName("FindByIdInvalid")
    class FindByIdInvalid {

        @Test
        void dadaRequisicaoComIdInexistente_quandoChamarFindById_entaoLancarException() {
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
package backend.finance.api_user.infrastructure.controllers;

import backend.finance.api_user.application.dtos.input.CustomerRequest;
import backend.finance.api_user.application.dtos.output.CustomerResponse;
import backend.finance.api_user.domain.enums.RoleEnum;
import backend.finance.api_user.infrastructure.repositories.CustomerRepository;
import backend.finance.api_user.utils.BaseIntegrationTest;
import backend.finance.api_user.utils.CustomerUtils;
import backend.finance.api_user.utils.UserUtils;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerControllerRestAssuredTest extends BaseIntegrationTest {

    private static final String URI_CUSTOMER = "/v1/customers";

    private static final String EMAIL_TESTE = "teste@email.com";

    private static final String USERNAME_TESTE = "username-teste";

    @LocalServerPort
    private int randomPort;

    @Autowired
    private CustomerController customerController;

    @Autowired
    private CustomerRepository customerRepository;

    private CustomerResponse defaultCustomerResponse;

    @BeforeEach
    void setUp() {
        RestAssured.port = randomPort; // Configura a porta dinâmica
        RestAssured.basePath = URI_CUSTOMER;

        var request =
                buildCustomerRequest(USERNAME_TESTE, "password123", RoleEnum.ROLE_CUSTOMER.getValue(),
                        "Anne Frank", EMAIL_TESTE);
        defaultCustomerResponse = customerController.create(request).getBody();
    }

    @AfterEach
    void tearDown() {
        customerRepository.deleteAll();
    }

    @Nested
    @DisplayName("CreateValid")
    class CreateValid {

        @Test
        void dadaRequisicaoValida_quandoChamarCreate_entaoCriarComSucesso() {
            var request =
                    buildCustomerRequest("johndoe", "password123", RoleEnum.ROLE_CUSTOMER.getValue(),
                            "John Doe", "doe@gmail.com");

            RestAssured.given()
                        .contentType(ContentType.JSON)
                        .body(request)
                    .when()
                        .post()
                    .then()
                        .statusCode(HttpStatus.CREATED.value())
                        .body("id", Matchers.notNullValue())
                        .body("name", Matchers.equalTo(request.name()))
                        .body("email", Matchers.equalTo(request.email()))
                        .body("active", Matchers.equalTo(true))
                        .body("user.id", Matchers.notNullValue())
                        .body("user.username", Matchers.equalTo(request.user().username()))
                        .body("user.active", Matchers.equalTo(true));
        }
    }

    @Nested
    @DisplayName("CreateInvalid")
    class CreateInvalid {

        @Test
        void dadaRequisicaoComEmailDuplicado_quandoChamarCreate_entaoDeveLancarException() {
            var request =
                    buildCustomerRequest("jeffbeck", "password123", RoleEnum.ROLE_ADMIN.getValue(),
                            "Jeff Beck", EMAIL_TESTE);

            RestAssured.given()
                        .contentType(ContentType.JSON)
                        .body(request)
                    .when()
                        .post()
                    .then()
                        .statusCode(HttpStatus.CONFLICT.value())
                        .body("title", Matchers.equalTo("Esse email já existe no sistema: " + EMAIL_TESTE + "."));
        }

        @Test
        void dadaRequisicaoComUsernameDuplicado_quandoChamarCreate_entaoDeveLancarException() {
            var request =
                    buildCustomerRequest(USERNAME_TESTE, "password123", RoleEnum.ROLE_CUSTOMER.getValue(),
                            "Jeff Beck", "jbeck@email.com");

            RestAssured.given()
                        .contentType(ContentType.JSON)
                        .body(request)
                    .when()
                        .post()
                    .then()
                        .statusCode(HttpStatus.CONFLICT.value())
                        .body("title", Matchers.equalTo("Esse username já existe no sistema: " + USERNAME_TESTE + "."));
        }

        @Test
        void dadaRequisicaoComRoleInvalid_quandoChamarCreate_entaoDeveLancarException() {
            var request =
                    buildCustomerRequest("jbeck123", "password123", "ROLE_INVALID",
                            "Jeff Beck", "jbeck@email.com");

            RestAssured.given()
                        .contentType(ContentType.JSON)
                        .body(request)
                    .when()
                        .post()
                    .then()
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .body("title", Matchers.equalTo("Permissão não encontrada pelo nome: ROLE_INVALID."));
        }
    }

    @Nested
    @DisplayName("UpdateValid")
    class UpdateValid {

        @Test
        void dadaRequisicaoValida_quandoChamarUpdate_entaoRetornarCustomerAtualizado() {
            var idCustomer = defaultCustomerResponse.id();

            var request =
                    buildCustomerRequest("anne_frank_atual", "password123", RoleEnum.ROLE_ADMIN.getValue(),
                            "Anne Atual Frank", "frank_atual@gmail.com");

            RestAssured.given()
                        .contentType(ContentType.JSON)
                        .body(request)
                    .when()
                        .put("/{id}", idCustomer)
                    .then()
                        .statusCode(HttpStatus.OK.value())
                        .body("id", Matchers.notNullValue())
                        .body("name", Matchers.equalTo(request.name()))
                        .body("email", Matchers.equalTo(request.email()))
                        .body("active", Matchers.equalTo(true))
                        .body("user.id", Matchers.notNullValue())
                        .body("user.username", Matchers.equalTo(request.user().username()))
                        .body("user.active", Matchers.equalTo(true));
        }
    }

    @Nested
    @DisplayName("UpdateInvalid")
    class UpdateInvalid {

        @Test
        void dadaRequisicaoInvalidaComIdInexistente_quandoChamarUpdate_entaoLancarException() {
            var idCustomerInvalid = UUID.randomUUID();

            var request =
                    buildCustomerRequest("robert_plant", "password123", RoleEnum.ROLE_CUSTOMER.getValue(),
                            "Robert Plant", "plant@gmail.com");

            RestAssured.given()
                        .contentType(ContentType.JSON)
                        .body(request)
                    .when()
                        .put("/{id}", idCustomerInvalid)
                    .then()
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .body("title", Matchers.equalTo("Cliente não encontrado por id: " + idCustomerInvalid + "."));
        }

        @Test
        void dadaRequisicaoInvalidaComEmailDuplicado_quandoChamarUpdate_entaoLancarException() {
            var emailDuplicate = "doe@gmail.com";
            var requestCreate =
                    buildCustomerRequest("johndoe", "password123", RoleEnum.ROLE_CUSTOMER.getValue(),
                            "John Doe", emailDuplicate);

            customerController.create(requestCreate);

            var idCustomer = defaultCustomerResponse.id();
            var requestUpdate =
                    buildCustomerRequest("anne_frank_atual", "password888", RoleEnum.ROLE_ADMIN.getValue(),
                            "Anne Atual Frank", emailDuplicate);

            RestAssured.given()
                        .contentType(ContentType.JSON)
                        .body(requestUpdate)
                    .when()
                        .put("/{id}", idCustomer)
                    .then()
                        .statusCode(HttpStatus.CONFLICT.value())
                        .body("title", Matchers.equalTo("Esse email já existe no sistema: " + emailDuplicate + "."));
        }

        @Test
        void dadaRequisicaoInvalidaComUsernameDuplicado_quandoChamarUpdate_entaoLancarException() {
            var usernameDuplicate = "johndoe";
            var requestCreate =
                    buildCustomerRequest(usernameDuplicate, "password123", RoleEnum.ROLE_CUSTOMER.getValue(),
                            "John Doe", "doe@gmail.com");

            customerController.create(requestCreate);

            var idCustomer = defaultCustomerResponse.id();
            var requestUpdate =
                    buildCustomerRequest(usernameDuplicate, "password888", RoleEnum.ROLE_ADMIN.getValue(),
                            "Anne Atual Frank", "frank_atual@gmail.com");

            RestAssured.given()
                        .contentType(ContentType.JSON)
                        .body(requestUpdate)
                    .when()
                        .put("/{id}", idCustomer)
                    .then()
                        .statusCode(HttpStatus.CONFLICT.value())
                        .body("title", Matchers.equalTo("Esse username já existe no sistema: " + usernameDuplicate + "."));
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
                        .body("title", Matchers.equalTo("Cliente não encontrado por id: " + idInexistente + "."));
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
                        .body("id", Matchers.notNullValue())
                        .body("name", Matchers.equalTo(defaultCustomerResponse.name()))
                        .body("email", Matchers.equalTo(defaultCustomerResponse.email()))
                        .body("active", Matchers.equalTo(true))
                        .body("user.id", Matchers.notNullValue())
                        .body("user.username", Matchers.equalTo(defaultCustomerResponse.user().username()))
                        .body("user.active", Matchers.equalTo(true));
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
                        .body("title", Matchers.equalTo("Cliente não encontrado por id: " + idNotFound + "."));
        }
    }

    private CustomerRequest buildCustomerRequest(String username, String password, String role, String name, String email) {
        var userRequest = UserUtils.trainRequest(username, password, role);
        return CustomerUtils.trainRequest(name, email, userRequest);
    }
}
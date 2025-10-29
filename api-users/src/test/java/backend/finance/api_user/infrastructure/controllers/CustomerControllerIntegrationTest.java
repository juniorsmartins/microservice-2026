package backend.finance.api_user.infrastructure.controllers;

import backend.finance.api_user.application.configs.exception.http404.CustomerNotFoundCustomException;
import backend.finance.api_user.application.configs.exception.http404.RoleNotFoundCustomException;
import backend.finance.api_user.application.configs.exception.http409.EmailConflictRulesCustomException;
import backend.finance.api_user.application.configs.exception.http409.UsernameConflictRulesCustomException;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerControllerIntegrationTest extends BaseIntegrationTest {

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

        var userRequest = UserUtils.trainRequest(USERNAME_TESTE, "password123", RoleEnum.ROLE_CUSTOMER.getValue());
        var customerRequest = CustomerUtils.trainRequest("Anne Frank", EMAIL_TESTE, userRequest);
        defaultCustomerResponse = customerController.create(customerRequest).getBody();
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

        @Test
        void dadaRequisicaoValida_quandoChamarCreate_entaoDeveSalvarNoBanco() {
            var request =
                    buildCustomerRequest("robertcm", "password123", RoleEnum.ROLE_CUSTOMER.getValue(),
                            "Robert C. Martin", "robert@gmail.com");

            var response = customerController.create(request).getBody();
            assertNotNull(response);
            var customerJpa = customerRepository.findById(response.id()).orElseThrow();

            assertEquals(request.name(), customerJpa.getName());
            assertEquals(request.email(), customerJpa.getEmail());
            assertTrue(customerJpa.isActive());
            assertEquals(request.user().username(), customerJpa.getUser().getUsername());
            assertEquals(request.user().password(), customerJpa.getUser().getPassword());
            assertTrue(customerJpa.getUser().isActive());
            assertEquals(RoleEnum.ROLE_CUSTOMER, customerJpa.getUser().getRole().getName());
        }

        @Test
        void dadaRequisicaoValidaComRoleAdmin_quandoChamarCreate_entaoDeveSalvarCustomerNoBanco() {
            var request =
                    buildCustomerRequest("robertcm", "password123", RoleEnum.ROLE_ADMIN.getValue(),
                            "Robert C. Martin", "robert@gmail.com");

            var response = customerController.create(request).getBody();
            assertNotNull(response);
            var customerJpa = customerRepository.findById(response.id()).orElseThrow();

            assertEquals(RoleEnum.ROLE_ADMIN, customerJpa.getUser().getRole().getName());
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
        void dadaRequisicaoComEmailDuplicado_quandoChamarCreate_entaoDeveLancarEmailConflictRulesCustomException() {
            var request =
                    buildCustomerRequest("jeffbeck", "password123", RoleEnum.ROLE_ADMIN.getValue(),
                            "Jeff Beck", EMAIL_TESTE);

            assertThrows(EmailConflictRulesCustomException.class, () -> customerController.create(request));
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
        void dadaRequisicaoComUsernameDuplicado_quandoChamarCreate_entaoDeveLancarUsernameConflictRulesCustomException() {
            var request =
                    buildCustomerRequest(USERNAME_TESTE, "password123", RoleEnum.ROLE_CUSTOMER.getValue(),
                            "Jeff Beck", "beck@gmail.com");

            assertThrows(UsernameConflictRulesCustomException.class, () -> customerController.create(request));
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

        @Test
        void dadaRequisicaoComRoleInvalid_quandoChamarCreate_entaoDeveLancarRoleNotFoundCustomException() {
            var request =
                    buildCustomerRequest("beck123", "password123", "ROLE_INVALID",
                            "Jeff Beck", "beck@gmail.com");

            assertThrows(RoleNotFoundCustomException.class, () -> customerController.create(request));
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

        @Test
        void dadaRequisicaoValida_quandoChamarUpdate_entaoSalvarNoBanco() {
            var idCustomer = defaultCustomerResponse.id();

            var request =
                    buildCustomerRequest("username999", "password999", RoleEnum.ROLE_ADMIN.getValue(),
                            "John Atual Doe", "doe@yahoo.com");

            var responseAtualizado = customerController.update(idCustomer, request).getBody();
            assertNotNull(responseAtualizado);
            var customerDoBanco = customerRepository.findById(idCustomer).orElseThrow();

            assertEquals(request.name(), customerDoBanco.getName());
            assertEquals(request.email(), customerDoBanco.getEmail());
            assertTrue(customerDoBanco.isActive());
            assertEquals(request.user().username(), customerDoBanco.getUser().getUsername());
            assertEquals(request.user().password(), customerDoBanco.getUser().getPassword());
            assertTrue(customerDoBanco.getUser().isActive());
            assertEquals(request.user().role(), customerDoBanco.getUser().getRole().getName().getValue());
        }

        @Test
        void dadaRequisicaoValidaSemAlterarEmailAndUsername_quandoChamarUpdate_entaoSalvarNoBanco() {
            var idCustomer = defaultCustomerResponse.id();

            var request =
                    buildCustomerRequest(USERNAME_TESTE, "atual123", RoleEnum.ROLE_ADMIN.getValue(),
                            "John Atual Doe", EMAIL_TESTE);

            var responseAtualizado = customerController.update(idCustomer, request).getBody();
            assertNotNull(responseAtualizado);
            var customerDoBanco = customerRepository.findById(idCustomer).orElseThrow();

            assertEquals(request.name(), customerDoBanco.getName());
            assertEquals(EMAIL_TESTE, customerDoBanco.getEmail());
            assertTrue(customerDoBanco.isActive());
            assertEquals(USERNAME_TESTE, customerDoBanco.getUser().getUsername());
            assertEquals(request.user().password(), customerDoBanco.getUser().getPassword());
            assertTrue(customerDoBanco.getUser().isActive());
            assertEquals(request.user().role(), customerDoBanco.getUser().getRole().getName().getValue());
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
        void dadaRequisicaoInvalidaComIdInexistente_quandoChamarUpdate_entaoLancarCustomerNotFoundCustomException() {
            var idCustomerInvalid = UUID.randomUUID();

            var request =
                    buildCustomerRequest("robert_plant", "password123", RoleEnum.ROLE_CUSTOMER.getValue(),
                            "Robert Plant", "plant@gmail.com");

            assertThrows(CustomerNotFoundCustomException.class, () -> customerController.update(idCustomerInvalid, request));
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
        void dadaRequisicaoInvalidaComEmailDuplicado_quandoChamarUpdate_entaoLancarEmailConflictRulesCustomException() {
            var emailDuplicate = "doe@gmail.com";
            var requestCreate =
                    buildCustomerRequest("johndoe", "password123", RoleEnum.ROLE_CUSTOMER.getValue(),
                            "John Doe", emailDuplicate);

            customerController.create(requestCreate);

            var idCustomer = defaultCustomerResponse.id();
            var requestUpdate =
                    buildCustomerRequest("anne_frank_atual", "password888", RoleEnum.ROLE_ADMIN.getValue(),
                            "Anne Atual Frank", emailDuplicate);

            assertThrows(EmailConflictRulesCustomException.class, () ->
                    customerController.update(idCustomer, requestUpdate));
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

        @Test
        void dadaRequisicaoInvalidaComUsernameDuplicado_quandoChamarUpdate_entaoLancarUsernameConflictRulesCustomException() {
            var usernameDuplicate = "johndoe";
            var requestCreate =
                    buildCustomerRequest(usernameDuplicate, "password123", RoleEnum.ROLE_CUSTOMER.getValue(),
                            "John Doe", "doe@gmail.com");

            customerController.create(requestCreate);

            var idCustomer = defaultCustomerResponse.id();
            var requestUpdate =
                    buildCustomerRequest(usernameDuplicate, "password888", RoleEnum.ROLE_ADMIN.getValue(),
                            "Anne Atual Frank", "frank_atual@gmail.com");

            assertThrows(UsernameConflictRulesCustomException.class, () ->
                    customerController.update(idCustomer, requestUpdate));
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

        @Test
        void dadaRequisicaoValida_quandoDeleteById_entaoArmazenarAtivoFalseNoBancoDeDados() {
            var customerActiveTrue = customerRepository.findById(defaultCustomerResponse.id());
            assertTrue(customerActiveTrue.isPresent());
            assertTrue(customerActiveTrue.get().isActive());

            RestAssured.given()
                        .contentType(ContentType.JSON)
                    .when()
                        .delete("/{id}", defaultCustomerResponse.id())
                    .then()
                        .statusCode(HttpStatus.NO_CONTENT.value());

            var customerActiveFalse = customerRepository.findById(defaultCustomerResponse.id()).orElseThrow();
            assertFalse(customerActiveFalse.isActive());
            assertFalse(customerActiveFalse.getUser().isActive());
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

        @Test
        void dadaRequisicaoComIdInexistente_quandoDeleteById_entaoLancarCustomerNotFoundCustomException() {
            var idNotFound = UUID.randomUUID();
            assertThrows(CustomerNotFoundCustomException.class, () -> customerController.disableById(idNotFound));
        }

        @Test
        void dadaRequisicaoComIdDesativado_quandoDeleteById_entaoLancarExceptionAndTerNoBancoComoFalse() {
            var idCustomer = defaultCustomerResponse.id();

            var customerBuscadoAntes = customerRepository.findById(idCustomer).orElseThrow();
            assertNotNull(customerBuscadoAntes);
            assertTrue(customerBuscadoAntes.isActive());

            var customerDesativado = customerController.disableById(idCustomer);
            assertEquals(HttpStatus.NO_CONTENT, customerDesativado.getStatusCode());

            RestAssured.given()
                        .contentType(ContentType.JSON)
                    .when()
                        .delete("/{id}", idCustomer)
                    .then()
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .body("title", Matchers.equalTo("Cliente não encontrado por id: " + idCustomer + "."));

            var customerBuscadoDepois = customerRepository.findById(idCustomer).orElseThrow();
            assertNotNull(customerBuscadoDepois);
            assertFalse(customerBuscadoDepois.isActive());
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

        @Test
        void dadaRequisicaoComIdInexistente_quandoChamarFindById_entaoLancarCustomerNotFoundCustomException() {
            var idNotFound = UUID.randomUUID();
            assertThrows(CustomerNotFoundCustomException.class, () -> customerController.findById(idNotFound));
        }

        @Test
        void dadaRequisicaoComIdDesativado_quandoConsultarPorId_entaoLancarExcecao() {
            var idCustomer = defaultCustomerResponse.id();

            var customerBuscadoAntes = customerRepository.findById(idCustomer).orElseThrow();
            assertNotNull(customerBuscadoAntes);
            assertTrue(customerBuscadoAntes.isActive());

            var customerDesativado = customerController.disableById(defaultCustomerResponse.id());
            assertEquals(HttpStatus.NO_CONTENT, customerDesativado.getStatusCode());

            RestAssured.given()
                        .contentType(ContentType.JSON)
                    .when()
                        .get("/{id}", defaultCustomerResponse.id())
                    .then()
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .body("title", Matchers.equalTo("Cliente não encontrado por id: " + defaultCustomerResponse.id() + "."));

            var customerBuscadoDepois = customerRepository.findById(idCustomer).orElseThrow();
            assertNotNull(customerBuscadoDepois);
            assertFalse(customerBuscadoDepois.isActive());
        }
    }

    private CustomerRequest buildCustomerRequest(String username, String password, String role, String name, String email) {
        var userRequest = UserUtils.trainRequest(username, password, role);
        return CustomerUtils.trainRequest(name, email, userRequest);
    }
}
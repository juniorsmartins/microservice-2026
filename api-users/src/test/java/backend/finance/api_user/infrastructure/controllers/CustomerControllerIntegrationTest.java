package backend.finance.api_user.infrastructure.controllers;

import backend.finance.api_user.application.configs.exception.http404.CustomerNotFoundCustomException;
import backend.finance.api_user.application.configs.exception.http404.RoleNotFoundCustomException;
import backend.finance.api_user.application.configs.exception.http409.EmailConflictRulesCustomException;
import backend.finance.api_user.application.configs.exception.http409.UsernameConflictRulesCustomException;
import backend.finance.api_user.application.dtos.input.CustomerRequest;
import backend.finance.api_user.application.dtos.output.CustomerResponse;
import backend.finance.api_user.domain.enums.RoleEnum;
import backend.finance.api_user.infrastructure.repositories.CustomerRepository;
import backend.finance.api_user.infrastructure.repositories.UserRepository;
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

    @Autowired
    private UserRepository userRepository;

    private CustomerRequest customerRequest;

    private CustomerResponse customerResponse;

    @BeforeEach
    void setUp() {
        RestAssured.port = randomPort; // Configura a porta dinâmica
        RestAssured.basePath = URI_CUSTOMER;

        var userRequest = UserUtils.trainRequest(USERNAME_TESTE, "password123", RoleEnum.ROLE_CUSTOMER.getValue());
        customerRequest = CustomerUtils.trainRequest("Anne Frank", EMAIL_TESTE, userRequest);
        customerResponse = customerController.create(customerRequest).getBody();
    }

    @AfterEach
    void tearDown() {
        customerRepository.deleteAll();
    }

    @Nested
    @DisplayName("CreateValid")
    class CreateValid {

        @Test
        void dadaRequisicaoValida_quandoChamarCreate_entaoDeveCriarAndDevolverCustomer() {
            // Arrange
            var userRequest = UserUtils
                    .trainRequest("johndoe", "password123", RoleEnum.ROLE_CUSTOMER.getValue());
            var request = CustomerUtils
                    .trainRequest("John Doe", "doe@gmail.com", userRequest);

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
                        .body("user.username", Matchers.equalTo(userRequest.username()))
                        .body("user.active", Matchers.equalTo(true));
        }

        @Test
        void dadaRequisicaoValidaComRoleCustomer_quandoChamarCreate_entaoDeveSalvarCustomerNoBanco() {
            // Arrange
            var userRequest = UserUtils.trainRequest("robertcm", "password123", RoleEnum.ROLE_CUSTOMER.getValue());
            var request = CustomerUtils.trainRequest("Robert C. Martin", "robert@gmail.com", userRequest);

            // Act
            var response = customerController.create(request);
            var customerJpa = customerRepository.findById(response.getBody().id()).orElseThrow();

            // Assert
            assertEquals(request.name(), customerJpa.getName());
            assertEquals(request.email(), customerJpa.getEmail());
            assertTrue(customerJpa.isActive());
            assertEquals(userRequest.username(), customerJpa.getUser().getUsername());
            assertEquals(userRequest.password(), customerJpa.getUser().getPassword());
            assertTrue(customerJpa.getUser().isActive());
            assertEquals(RoleEnum.ROLE_CUSTOMER, customerJpa.getUser().getRole().getName());
        }

        @Test
        void dadaRequisicaoValidaComRoleAdmin_quandoChamarCreate_entaoDeveSalvarCustomerNoBanco() {
            // Arrange
            var userRequest = UserUtils.trainRequest("robertcm", "password123", RoleEnum.ROLE_ADMIN.getValue());
            var request = CustomerUtils.trainRequest("Robert C. Martin", "robert@gmail.com", userRequest);

            // Act
            var response = customerController.create(request);
            var customerJpa = customerRepository.findById(response.getBody().id()).orElseThrow();

            // Assert
            assertEquals(RoleEnum.ROLE_ADMIN, customerJpa.getUser().getRole().getName());
        }
    }

    @Nested
    @DisplayName("CreateInvalid")
    class CreateInvalid {

        @Test
        void dadaRequisicaoComEmailDuplicado_quandoChamarCreate_entaoDeveLancarException() {
            // Arrange
            var userRequest = UserUtils.trainRequest("jeffbeck", "password123", RoleEnum.ROLE_CUSTOMER.getValue());
            var request = CustomerUtils.trainRequest("Jeff Beck", EMAIL_TESTE, userRequest);

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
            // Arrange
            var userRequest = UserUtils.trainRequest("jeffbeck", "password123", RoleEnum.ROLE_CUSTOMER.getValue());
            var request = CustomerUtils.trainRequest("Jeff Beck", EMAIL_TESTE, userRequest);

            // Act & Assert
            assertThrows(EmailConflictRulesCustomException.class, () -> customerController.create(request));
        }

        @Test
        void dadaRequisicaoComUsernameDuplicado_quandoChamarCreate_entaoDeveLancarException() {
            // Arrange
            var userRequest = UserUtils.trainRequest(USERNAME_TESTE, "password123", RoleEnum.ROLE_CUSTOMER.getValue());
            var request = CustomerUtils.trainRequest("Jeff Beck", "jbeck@email.com", userRequest);

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
            // Arrange
            var userRequest = UserUtils.trainRequest(USERNAME_TESTE, "password123", RoleEnum.ROLE_CUSTOMER.getValue());
            var request = CustomerUtils.trainRequest("Jeff Beck", "beck@gmail.com", userRequest);
            // Act & Assert
            assertThrows(UsernameConflictRulesCustomException.class, () -> customerController.create(request));
        }

        @Test
        void dadaRequisicaoComRoleInvalid_quandoChamarCreate_entaoDeveLancarException() {
            // Arrange
            var userRequest = UserUtils.trainRequest("jbeck123", "password123", "ROLE_INVALID");
            var request = CustomerUtils.trainRequest("Jeff Beck", "jbeck@email.com", userRequest);

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
            // Arrange
            var userRequest = UserUtils.trainRequest("beck123", "password123", "ROLE_INVALID");
            var request = CustomerUtils.trainRequest("Jeff Beck", "beck@gmail.com", userRequest);
            // Act & Assert
            assertThrows(RoleNotFoundCustomException.class, () -> customerController.create(request));
        }
    }

    @Nested
    @DisplayName("UpdateValid")
    class UpdateValid {

        @Test
        void dadaRequisicaoValida_quandoChamarUpdate_entaoRetornarCustomerAtualizado() {
            // Arrange
            var idCustomer = customerResponse.id();
            var userRequestUp = UserUtils.trainRequest("anne_frank_atual", "password123", RoleEnum.ROLE_ADMIN.getValue());
            var request = CustomerUtils.trainRequest("Anne Atual Frank", "frank_atual@gmail.com", userRequestUp);

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
                        .body("user.username", Matchers.equalTo(userRequestUp.username()))
                        .body("user.active", Matchers.equalTo(true));
        }

        @Test
        void dadaRequisicaoValidaSemAlterarEmailAndUsername_quandoChamarUpdate_entaoSalvarNoBanco() {
            var emailEqual = "doe@gmail.com";
            var usernameEqual = "johndoe";

            var userRequestCreate = UserUtils.trainRequest(usernameEqual, "password123", RoleEnum.ROLE_CUSTOMER.getValue());
            var customerRequestCreate = CustomerUtils.trainRequest("John Doe", emailEqual, userRequestCreate);
            var responseCreate = customerController.create(customerRequestCreate).getBody();

            var userRequestUpdate = UserUtils.trainRequest(usernameEqual, "atual123", RoleEnum.ROLE_ADMIN.getValue());
            var customerRequestUpdate = CustomerUtils.trainRequest("John Atual Doe", emailEqual, userRequestUpdate);
            var responseUpdate = customerController.update(responseCreate.id(), customerRequestUpdate).getBody();

            var customerDoBanco = customerRepository.findById(responseUpdate.id()).orElseThrow();

            assertEquals(customerRequestUpdate.name(), customerDoBanco.getName());
            assertEquals(emailEqual, customerDoBanco.getEmail());
            assertTrue(customerDoBanco.isActive());
            assertEquals(usernameEqual, customerDoBanco.getUser().getUsername());
            assertEquals(userRequestUpdate.password(), customerDoBanco.getUser().getPassword());
            assertTrue(customerDoBanco.getUser().isActive());
            assertEquals(userRequestUpdate.role(), customerDoBanco.getUser().getRole().getName().getValue());
        }

        @Test
        void dadaRequisicaoValida_quandoChamarUpdate_entaoSalvarCustomerAtualizadoNoBanco() {
            var userRequestCreate = UserUtils.trainRequest("martin999", "password999", RoleEnum.ROLE_CUSTOMER.getValue());
            var customerRequestCreate = CustomerUtils.trainRequest("Robert Martin", "martin9@email.com", userRequestCreate);
            var responseCreate = customerController.create(customerRequestCreate).getBody();

            var customerDoBancoAntes = customerRepository.findById(responseCreate.id()).orElseThrow();
            assertEquals(RoleEnum.ROLE_CUSTOMER.getValue(), customerDoBancoAntes.getUser().getRole().getName().getValue());

            var userRequestUpdate = UserUtils.trainRequest("martin123", "password888", RoleEnum.ROLE_ADMIN.getValue());
            var customerRequestUpdate = CustomerUtils.trainRequest("Robert Cecil Martin", "rcm@email.com", userRequestUpdate);
            var responseUpdate = customerController.update(responseCreate.id(), customerRequestUpdate).getBody();

            var customerDoBanco = customerRepository.findById(responseCreate.id()).orElseThrow();

            assertEquals(responseUpdate.name(), customerDoBanco.getName());
            assertEquals(responseUpdate.email(), customerDoBanco.getEmail());
            assertTrue(customerDoBanco.isActive());
            assertEquals(responseUpdate.user().username(), customerDoBanco.getUser().getUsername());
            assertEquals(userRequestUpdate.password(), customerDoBanco.getUser().getPassword());
            assertTrue(customerDoBanco.getUser().isActive());
            assertEquals(RoleEnum.ROLE_ADMIN.getValue(), customerDoBanco.getUser().getRole().getName().getValue());
        }
    }

    @Nested
    @DisplayName("UpdateInvalid")
    class UpdateInvalid {

        @Test
        void dadaRequisicaoInvalidaComIdInexistente_quandoChamarUpdate_entaoLancarException() {
            var idCustomerInvalid = UUID.randomUUID();
            var userRequestUp = UserUtils.trainRequest("robert_plant", "password123", RoleEnum.ROLE_ADMIN.getValue());
            var customerRequestUp = CustomerUtils.trainRequest("Robert Plant", "plant@gmail.com", userRequestUp);

            RestAssured.given()
                        .contentType(ContentType.JSON)
                        .body(customerRequestUp)
                    .when()
                        .put("/{id}", idCustomerInvalid)
                    .then()
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .body("title", Matchers.equalTo("Cliente não encontrado por id: " + idCustomerInvalid + "."));
        }

        @Test
        void dadaRequisicaoInvalidaComIdInexistente_quandoChamarUpdate_entaoLancarCustomerNotFoundCustomException() {
            // Arrange
            var idCustomerInvalid = UUID.randomUUID();
            var userRequestUp = UserUtils
                    .trainRequest("robert_plant", "password123", RoleEnum.ROLE_ADMIN.getValue());
            var customerRequestUp = CustomerUtils
                    .trainRequest("Robert Plant", "plant@gmail.com", userRequestUp);
            // Act & Assert
            assertThrows(CustomerNotFoundCustomException.class, () -> customerController.update(idCustomerInvalid, customerRequestUp));
        }

        @Test
        void dadaRequisicaoInvalidaComEmailDuplicado_quandoChamarUpdate_entaoLancarException() {
            var emailDuplicate = "doe@gmail.com";
            var userRequest = UserUtils.trainRequest("johndoe", "password123", RoleEnum.ROLE_CUSTOMER.getValue());
            var request = CustomerUtils.trainRequest("John Doe", emailDuplicate, userRequest);
            customerController.create(request);

            var idCustomer = customerResponse.id();
            var userRequestUp = UserUtils.trainRequest("anne_frank_atual", "password123", RoleEnum.ROLE_ADMIN.getValue());
            var customerRequestUp = CustomerUtils.trainRequest("Anne Atual Frank", emailDuplicate, userRequestUp);

            RestAssured.given()
                        .contentType(ContentType.JSON)
                        .body(customerRequestUp)
                    .when()
                        .put("/{id}", idCustomer)
                    .then()
                        .statusCode(HttpStatus.CONFLICT.value())
                        .body("title", Matchers.equalTo("Esse email já existe no sistema: " + emailDuplicate + "."));
        }

        @Test
        void dadaRequisicaoInvalidaComEmailDuplicado_quandoChamarUpdate_entaoLancarEmailConflictRulesCustomException() {
            var emailDuplicate = "doe@gmail.com";
            var userRequest = UserUtils.trainRequest("johndoe", "password123", RoleEnum.ROLE_CUSTOMER.getValue());
            var request = CustomerUtils.trainRequest("John Doe", emailDuplicate, userRequest);
            customerController.create(request);

            var idCustomer = customerResponse.id();
            var userRequestUp = UserUtils.trainRequest("anne_frank_atual", "password123", RoleEnum.ROLE_ADMIN.getValue());
            var customerRequestUp = CustomerUtils.trainRequest("Anne Atual Frank", emailDuplicate, userRequestUp);

            assertThrows(EmailConflictRulesCustomException.class, () -> customerController.update(idCustomer, customerRequestUp));
        }

        @Test
        void dadaRequisicaoInvalidaComUsernameDuplicado_quandoChamarUpdate_entaoLancarException() {
            var usernameDuplicate = "johndoe";
            var userRequest = UserUtils.trainRequest(usernameDuplicate, "password123", RoleEnum.ROLE_CUSTOMER.getValue());
            var request = CustomerUtils.trainRequest("John Doe", "doe@gmail.com", userRequest);
            customerController.create(request);

            var idCustomer = customerResponse.id();
            var userRequestUp = UserUtils.trainRequest(usernameDuplicate, "password123", RoleEnum.ROLE_ADMIN.getValue());
            var customerRequestUp = CustomerUtils.trainRequest("Anne Atual Frank", "frank_atual@gmail.com", userRequestUp);

            RestAssured.given()
                        .contentType(ContentType.JSON)
                        .body(customerRequestUp)
                    .when()
                        .put("/{id}", idCustomer)
                    .then()
                        .statusCode(HttpStatus.CONFLICT.value())
                        .body("title", Matchers.equalTo("Esse username já existe no sistema: " + usernameDuplicate + "."));
        }

        @Test
        void dadaRequisicaoInvalidaComUsernameDuplicado_quandoChamarUpdate_entaoLancarUsernameConflictRulesCustomException() {
            var usernameDuplicate = "johndoe";
            var userRequest = UserUtils.trainRequest(usernameDuplicate, "password123", RoleEnum.ROLE_CUSTOMER.getValue());
            var request = CustomerUtils.trainRequest("John Doe", "doe@gmail.com", userRequest);
            customerController.create(request);

            var idCustomer = customerResponse.id();
            var userRequestUp = UserUtils.trainRequest(usernameDuplicate, "password123", RoleEnum.ROLE_ADMIN.getValue());
            var customerRequestUp = CustomerUtils.trainRequest("Anne Atual Frank", "frank_atual@gmail.com", userRequestUp);

            assertThrows(UsernameConflictRulesCustomException.class, () -> customerController.update(idCustomer, customerRequestUp));
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
                        .delete("/{id}", customerResponse.id())
                    .then()
                        .statusCode(HttpStatus.NO_CONTENT.value());
        }

        @Test
        void dadaRequisicaoValida_quandoDeleteById_entaoArmazenarAtivoFalseNoBancoDeDados() {
            var customerActiveTrue = customerRepository.findById(customerResponse.id());
            assertTrue(customerActiveTrue.isPresent());
            assertTrue(customerActiveTrue.get().isActive());

            RestAssured.given()
                        .contentType(ContentType.JSON)
                    .when()
                        .delete("/{id}", customerResponse.id())
                    .then()
                        .statusCode(HttpStatus.NO_CONTENT.value());

            var customerActiveFalse = customerRepository.findById(customerResponse.id()).orElseThrow();
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
            var idCustomer = customerResponse.id();

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
                        .get("/{id}", customerResponse.id())
                    .then()
                        .statusCode(HttpStatus.OK.value())
                        .body("id", Matchers.notNullValue())
                        .body("name", Matchers.equalTo(customerResponse.name()))
                        .body("email", Matchers.equalTo(customerResponse.email()))
                        .body("active", Matchers.equalTo(true))
                        .body("user.id", Matchers.notNullValue())
                        .body("user.username", Matchers.equalTo(customerResponse.user().username()))
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
            var idCustomer = customerResponse.id();

            var customerBuscadoAntes = customerRepository.findById(idCustomer).orElseThrow();
            assertNotNull(customerBuscadoAntes);
            assertTrue(customerBuscadoAntes.isActive());

            var customerDesativado = customerController.disableById(customerResponse.id());
            assertEquals(HttpStatus.NO_CONTENT, customerDesativado.getStatusCode());

            RestAssured.given()
                        .contentType(ContentType.JSON)
                    .when()
                        .get("/{id}", customerResponse.id())
                    .then()
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .body("title", Matchers.equalTo("Cliente não encontrado por id: " + customerResponse.id() + "."));

            var customerBuscadoDepois = customerRepository.findById(idCustomer).orElseThrow();
            assertNotNull(customerBuscadoDepois);
            assertFalse(customerBuscadoDepois.isActive());
        }
    }
}
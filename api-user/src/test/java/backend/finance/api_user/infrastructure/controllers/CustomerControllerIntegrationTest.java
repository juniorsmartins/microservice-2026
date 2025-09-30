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
import backend.finance.api_user.utils.CustomerUtils;
import backend.finance.api_user.utils.UserUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CustomerControllerIntegrationTest {

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
        var userRequest = UserUtils.trainUserRequest("anne_frank", "password123", RoleEnum.ROLE_CUSTOMER.getValue());
        customerRequest = CustomerUtils.trainCustomerRequest("Anne Frank", "frank@gmail.com", userRequest);
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
            var userRequest = UserUtils.trainUserRequest("johndoe", "password123", RoleEnum.ROLE_CUSTOMER.getValue());
            var request = CustomerUtils.trainCustomerRequest("John Doe", "doe@gmail.com", userRequest);
            // Act
            var responseEntity = customerController.create(request);
            // Assert
            assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
            var customerResponse = responseEntity.getBody();
            assertEquals(userRequest.username(), customerResponse.user().username());
            assertEquals(request.name(), customerResponse.name());
            assertEquals(request.email(), customerResponse.email());
        }

        @Test
        void dadaRequisicaoValidaComRoleCustomer_quandoChamarCreate_entaoDeveSalvarCustomerNoBanco() {
            // Arrange
            var userRequest = UserUtils.trainUserRequest("robertcm", "password123", RoleEnum.ROLE_CUSTOMER.getValue());
            var request = CustomerUtils.trainCustomerRequest("Robert C. Martin", "robert@gmail.com", userRequest);
            // Act
            var response = customerController.create(request);
            var customerJpa = customerRepository.findById(response.getBody().id()).orElseThrow();
            // Assert
            assertEquals(userRequest.username(), customerJpa.getUser().getUsername());
            assertEquals(request.name(), customerJpa.getName());
            assertEquals(request.email(), customerJpa.getEmail());
            assertEquals(RoleEnum.ROLE_CUSTOMER, customerJpa.getUser().getRole().getName());
        }

        @Test
        void dadaRequisicaoValidaComRoleAdmin_quandoChamarCreate_entaoDeveSalvarCustomerNoBanco() {
            // Arrange
            var userRequest = UserUtils.trainUserRequest("robertcm", "password123", RoleEnum.ROLE_ADMIN.getValue());
            var request = CustomerUtils.trainCustomerRequest("Robert C. Martin", "robert@gmail.com", userRequest);
            // Act
            var response = customerController.create(request);
            var customerJpa = customerRepository.findById(response.getBody().id()).orElseThrow();
            // Assert
            assertEquals(userRequest.username(), customerJpa.getUser().getUsername());
            assertEquals(request.name(), customerJpa.getName());
            assertEquals(request.email(), customerJpa.getEmail());
            assertEquals(RoleEnum.ROLE_ADMIN, customerJpa.getUser().getRole().getName());
        }
    }

    @Nested
    @DisplayName("CreateInvalid")
    class CreateInvalid {

        @Test
        void dadaRequisicaoInvalidaComEmailDuplicado_quandoChamarCreate_entaoDeveLancarException() {
            // Arrange
            var emailDuplicate = "jeff@gmail.com";
            var userRequest1 = UserUtils.trainUserRequest("jeffbeck", "password123", RoleEnum.ROLE_CUSTOMER.getValue());
            var request1 = CustomerUtils.trainCustomerRequest("Jeff Beck", emailDuplicate, userRequest1);
            var userRequest2 = UserUtils.trainUserRequest("sutherland", "password123", RoleEnum.ROLE_CUSTOMER.getValue());
            var request2 = CustomerUtils.trainCustomerRequest("Jeff Sutherland", emailDuplicate, userRequest2);
            // Act & Assert
            customerController.create(request1);
            assertThrows(EmailConflictRulesCustomException.class, () -> customerController.create(request2));
        }

        @Test
        void dadaRequisicaoInvalidaComUsernameDuplicado_quandoChamarCreate_entaoDeveLancarException() {
            // Arrange
            var usernameDuplicate = "jeff123";
            var userRequest1 = UserUtils.trainUserRequest(usernameDuplicate, "password123", RoleEnum.ROLE_CUSTOMER.getValue());
            var request1 = CustomerUtils.trainCustomerRequest("Jeff Beck", "beck@gmail.com", userRequest1);
            var userRequest2 = UserUtils.trainUserRequest(usernameDuplicate, "password123", RoleEnum.ROLE_CUSTOMER.getValue());
            var request2 = CustomerUtils.trainCustomerRequest("Jeff Sutherland", "sutherland@gmail.com", userRequest2);
            // Act & Assert
            customerController.create(request1);
            assertThrows(UsernameConflictRulesCustomException.class, () -> customerController.create(request2));
        }

        @Test
        void dadaRequisicaoInvalidaComRoleInvalid_quandoChamarCreate_entaoDeveLancarException() {
            // Arrange
            var roleInvalid = "ROLE_INVALID";
            var userRequest = UserUtils.trainUserRequest("beck123", "password123", roleInvalid);
            var request = CustomerUtils.trainCustomerRequest("Jeff Beck", "beck@gmail.com", userRequest);
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
            var userRequestUp = UserUtils.trainUserRequest("anne_frank_atual", "password123", RoleEnum.ROLE_ADMIN.getValue());
            var customerRequestUp = CustomerUtils.trainCustomerRequest("Anne Atual Frank", "frank_atual@gmail.com", userRequestUp);
            // Act
            var responseEntity = customerController.update(idCustomer, customerRequestUp);
            // Assert
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            var customerResponseUp = responseEntity.getBody();
            assertEquals(userRequestUp.username(), customerResponseUp.user().username());
            assertEquals(customerRequestUp.name(), customerResponseUp.name());
            assertEquals(customerRequestUp.email(), customerResponseUp.email());
        }

        @Test
        void dadaRequisicaoValidaSemAlterarEmailAndUsername_quandoChamarUpdate_entaoSalvarNoBanco() {
            var emailEqual = "doe@gmail.com";
            var usernameEqual = "johndoe";

            var userRequestCreate = UserUtils.trainUserRequest(usernameEqual, "password123", RoleEnum.ROLE_CUSTOMER.getValue());
            var customerRequestCreate = CustomerUtils.trainCustomerRequest("John Doe", emailEqual, userRequestCreate);
            var responseCreate = customerController.create(customerRequestCreate).getBody();

            var userRequestUpdate = UserUtils.trainUserRequest(usernameEqual, "atual123", RoleEnum.ROLE_ADMIN.getValue());
            var customerRequestUpdate = CustomerUtils.trainCustomerRequest("John Atual Doe", emailEqual, userRequestUpdate);
            var responseUpdate = customerController.update(responseCreate.id(), customerRequestUpdate).getBody();

            var customerDb = customerRepository.findById(responseUpdate.id()).orElseThrow();

            assertEquals(userRequestUpdate.role(), customerDb.getUser().getRole().getName().getValue());
            assertEquals(usernameEqual, customerDb.getUser().getUsername());
            assertEquals(userRequestUpdate.password(), customerDb.getUser().getPassword());
            assertEquals(customerRequestUpdate.name(), customerDb.getName());
            assertEquals(emailEqual, customerDb.getEmail());
        }

        @Test
        void dadaRequisicaoValida_quandoChamarUpdate_entaoSalvarCustomerAtualizadoNoBanco() {
            // Arrange
            var idCustomer = customerResponse.id();
            var userRequestUp = UserUtils.trainUserRequest("anne_frank_atual", "password123", RoleEnum.ROLE_ADMIN.getValue());
            var customerRequestUp = CustomerUtils.trainCustomerRequest("Anne Atual Frank", "frank_atual@gmail.com", userRequestUp);
            // Act
            customerController.update(idCustomer, customerRequestUp);
            var customerUpdate = customerRepository.findById(idCustomer).orElseThrow();
            // Assert
            assertEquals(userRequestUp.role(), customerUpdate.getUser().getRole().getName().getValue());
            assertEquals(userRequestUp.username(), customerUpdate.getUser().getUsername());
            assertEquals(customerRequestUp.name(), customerUpdate.getName());
            assertEquals(customerRequestUp.email(), customerUpdate.getEmail());
        }
    }

    @Nested
    @DisplayName("UpdateInvalid")
    class UpdateInvalid {

        @Test
        void dadaRequisicaoInvalidaComIdInexistente_quandoChamarUpdate_entaoLancarException() {
            // Arrange
            var idCustomerInvalid = UUID.randomUUID();
            var userRequestUp = UserUtils.trainUserRequest("robert_plant", "password123", RoleEnum.ROLE_ADMIN.getValue());
            var customerRequestUp = CustomerUtils.trainCustomerRequest("Robert Plant", "plant@gmail.com", userRequestUp);
            // Act & Assert
            assertThrows(CustomerNotFoundCustomException.class, () -> customerController.update(idCustomerInvalid, customerRequestUp));
        }

        @Test
        void dadaRequisicaoInvalidaComEmailDuplicado_quandoChamarUpdate_entaoLancarException() {
            var emailDuplicate = "doe@gmail.com";
            var userRequest = UserUtils.trainUserRequest("johndoe", "password123", RoleEnum.ROLE_CUSTOMER.getValue());
            var request = CustomerUtils.trainCustomerRequest("John Doe", emailDuplicate, userRequest);
            customerController.create(request);

            var idCustomer = customerResponse.id();
            var userRequestUp = UserUtils.trainUserRequest("anne_frank_atual", "password123", RoleEnum.ROLE_ADMIN.getValue());
            var customerRequestUp = CustomerUtils.trainCustomerRequest("Anne Atual Frank", emailDuplicate, userRequestUp);

            assertThrows(EmailConflictRulesCustomException.class, () -> customerController.update(idCustomer, customerRequestUp));
        }

        @Test
        void dadaRequisicaoInvalidaComUsernameDuplicado_quandoChamarUpdate_entaoLancarException() {
            var usernameDuplicate = "johndoe";
            var userRequest = UserUtils.trainUserRequest(usernameDuplicate, "password123", RoleEnum.ROLE_CUSTOMER.getValue());
            var request = CustomerUtils.trainCustomerRequest("John Doe", "doe@gmail.com", userRequest);
            customerController.create(request);

            var idCustomer = customerResponse.id();
            var userRequestUp = UserUtils.trainUserRequest(usernameDuplicate, "password123", RoleEnum.ROLE_ADMIN.getValue());
            var customerRequestUp = CustomerUtils.trainCustomerRequest("Anne Atual Frank", "frank_atual@gmail.com", userRequestUp);

            assertThrows(UsernameConflictRulesCustomException.class, () -> customerController.update(idCustomer, customerRequestUp));
        }
    }

    @Nested
    @DisplayName("DeleteValid")
    class DeleteValid {

        @Test
        void dadaRequisicaoValida_quandoChamarDeleteById_entaoRetornarHttpNoContent() {
            var idValid = customerResponse.id();
            var response = customerController.deleteById(idValid);
            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        }

        @Test
        void dadaRequisicaoValida_quandoChamarDeleteById_entaoDeletarCustomerAndUser() {
            var idCustomerValid = customerResponse.id();
            var customerDoBancoAntes = customerRepository.findById(idCustomerValid);
            assertTrue(customerDoBancoAntes.isPresent());

            var idUserValid = customerResponse.user().id();
            var userDoBancoAntes = userRepository.findById(idUserValid);
            assertTrue(userDoBancoAntes.isPresent());

            customerController.deleteById(idCustomerValid);

            var customerDoBancoDepois = customerRepository.findById(idCustomerValid);
            assertTrue(customerDoBancoDepois.isEmpty());

            var userDoBancoDepois = userRepository.findById(idUserValid);
            assertTrue(userDoBancoDepois.isEmpty());
        }
    }

    @Nested
    @DisplayName("DeleteInvalid")
    class DeleteInvalid {

        @Test
        void dadaRequisicaoInvalidaComIdInexistente_quandoChamarDeleteById_entaoLancarException() {
            var idNotFound = UUID.randomUUID();
            assertThrows(CustomerNotFoundCustomException.class, () -> customerController.deleteById(idNotFound));
        }
    }

    @Nested
    @DisplayName("FindByIdValid")
    class FindByIdValid {

        @Test
        void dadaRequisicaoValida_quandoChamarFindById_entaoRetornarCustomer() {
            // Arrange
            var customerId = customerResponse.id();
            // Act
            var responseFind = customerController.findById(customerId);
            // Assert
            var customerResponse = responseFind.getBody();
            assertEquals(customerRequest.user().username(), customerResponse.user().username());
            assertEquals(customerRequest.name(), customerResponse.name());
            assertEquals(customerRequest.email(), customerResponse.email());
        }
    }

    @Nested
    @DisplayName("FindByIdInvalid")
    class FindByIdInvalid {

        @Test
        void dadaRequisicaoInvalidaComIdInexistente_quandoChamarFindById_entaoLancarException() {
            var idNotFound = UUID.randomUUID();
            assertThrows(CustomerNotFoundCustomException.class, () -> customerController.findById(idNotFound));
        }
    }
}
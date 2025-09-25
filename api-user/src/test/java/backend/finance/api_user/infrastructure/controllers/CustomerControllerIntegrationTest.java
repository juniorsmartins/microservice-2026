package backend.finance.api_user.infrastructure.controllers;

import backend.finance.api_user.application.configs.exception.http404.RoleNotFoundCustomException;
import backend.finance.api_user.application.configs.exception.http409.EmailConflictRulesCustomException;
import backend.finance.api_user.application.configs.exception.http409.UsernameConflictRulesCustomException;
import backend.finance.api_user.domain.enums.RoleEnum;
import backend.finance.api_user.infrastructure.repositories.CustomerRepository;
import backend.finance.api_user.utils.CustomerUtils;
import backend.finance.api_user.utils.UserUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class CustomerControllerIntegrationTest {

    @Autowired
    private CustomerController customerController;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
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
            var userRequest = UserUtils.trainUserRequest( "robertcm", "password123", RoleEnum.ROLE_CUSTOMER.getValue());
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
            var userRequest = UserUtils.trainUserRequest( "robertcm", "password123", RoleEnum.ROLE_ADMIN.getValue());
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
    @DisplayName("Update")
    class Update {
    }

    @Nested
    @DisplayName("Delete")
    class Delete {
    }

    @Nested
    @DisplayName("FindById")
    class FindById {
    }
}
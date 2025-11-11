package backend.finance.application.usecases;

import backend.finance.application.exceptions.http404.CustomerNotFoundCustomException;
import backend.finance.application.mappers.CustomerMapperImpl;
import backend.finance.application.mappers.RoleMapperImpl;
import backend.finance.application.mappers.UserMapperImpl;
import backend.finance.application.ports.output.CustomerQueryOutputPort;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static backend.finance.application.utils.CustomerTestFactory.defaultDto;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@Tag("Customer")
@Tag("UseCase")
class CustomerQueryUseCaseTest {

    @Mock
    private CustomerQueryOutputPort customerQueryOutputPort;

    @InjectMocks
    private CustomerQueryUseCase customerQueryUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        var roleMapper = new RoleMapperImpl();
        var userMapper = new UserMapperImpl(roleMapper);
        var customerMapper = new CustomerMapperImpl(userMapper);
        customerQueryUseCase = new CustomerQueryUseCase(customerQueryOutputPort, customerMapper);
    }

    @Nested
    @Order(1)
    @DisplayName("Query - casos válidos.")
    @Tag("QueryValid")
    class QueryValid {

        @Test
        @DisplayName("Deve consultar customer ativo por ID.")
        void deveConsultarCustomerAtivoPorId() {
            var customerDto = defaultDto();
            when(customerQueryOutputPort.findActiveById(customerDto.id())).thenReturn(Optional.of(customerDto));

            assertDoesNotThrow(() -> customerQueryUseCase.findActiveById(customerDto.id()));

            verify(customerQueryOutputPort, times(1)).findActiveById(customerDto.id());
        }
    }

    @Nested
    @Order(2)
    @DisplayName("Query - casos inválidos.")
    @Tag("QueryInvalid")
    class QueryInvalid {

        @Test
        @DisplayName("Deve lançar exceção quando consultar customer ativo por ID inexistente.")
        void deveConsultarCustomerAtivoPorIdInexistente() {
            var idInexistente = UUID.randomUUID();
            when(customerQueryOutputPort.findActiveById(any())).thenReturn(Optional.empty());

            var excecao = assertThrows(CustomerNotFoundCustomException.class, () ->
                    customerQueryUseCase.findActiveById(idInexistente));

            assertEquals("exception.resource.not-found.customer", excecao.getMessage());
            assertEquals(idInexistente.toString(), excecao.getValue());
            verify(customerQueryOutputPort, times(1)).findActiveById(idInexistente);
        }
    }
}

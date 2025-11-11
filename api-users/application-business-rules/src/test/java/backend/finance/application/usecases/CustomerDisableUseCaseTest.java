package backend.finance.application.usecases;

import backend.finance.application.dtos.CustomerDto;
import backend.finance.application.exceptions.http404.CustomerNotFoundCustomException;
import backend.finance.application.mappers.CustomerMapperImpl;
import backend.finance.application.mappers.RoleMapperImpl;
import backend.finance.application.mappers.UserMapperImpl;
import backend.finance.application.ports.output.CustomerQueryOutputPort;
import backend.finance.application.ports.output.CustomerSaveOutputPort;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static backend.finance.application.utils.CustomerTestFactory.defaultDto;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@Tag("Customer")
@Tag("UseCase")
class CustomerDisableUseCaseTest {

    @Mock
    private CustomerQueryOutputPort customerQueryOutputPort;

    @Mock
    private CustomerSaveOutputPort customerSaveOutputPort;

    @InjectMocks
    private CustomerDisableUseCase customerDisableUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        var roleMapper = new RoleMapperImpl();
        var userMapper = new UserMapperImpl(roleMapper);
        var customerMapper = new CustomerMapperImpl(userMapper);
        customerDisableUseCase = new CustomerDisableUseCase(customerQueryOutputPort, customerSaveOutputPort, customerMapper);
    }

    @Nested
    @Order(1)
    @DisplayName("Disable - casos válidos.")
    @Tag("DisableValid")
    class DisableValid {

        @Test
        @DisplayName("Deve desabilitar customer.")
        void deveDesabilitarCustomer() {
            var dtoTrue = defaultDto();
            when(customerQueryOutputPort.findByIdAndActiveTrue(dtoTrue.id())).thenReturn(Optional.of(dtoTrue));

            assertTrue(dtoTrue.active());
            assertTrue(dtoTrue.user().active());

            customerDisableUseCase.disableById(dtoTrue.id());

            ArgumentCaptor<CustomerDto> captor = ArgumentCaptor.forClass(CustomerDto.class);
            verify(customerSaveOutputPort, times(1)).save(captor.capture());

            CustomerDto salvo = captor.getValue();
            assertAll("DTO salvo deve estar desativado",
                    () -> assertFalse(salvo.active(), "Customer deve estar inativo"),
                    () -> assertFalse(salvo.user().active(), "User deve estar inativo")
            );
        }
    }

    @Nested
    @Order(2)
    @DisplayName("Disable - casos inválidos.")
    @Tag("DisableInvalid")
    class DisableInvalid {

        @Test
        @DisplayName("Deve lançar exceçao quando não encontrar customer.")
        void deveLancarExcecaoPorNaoEncontrarCustomer() {
            var idInexistente = UUID.randomUUID();
            when(customerQueryOutputPort.findByIdAndActiveTrue(any())).thenReturn(Optional.empty());

            var excecao = assertThrows(CustomerNotFoundCustomException.class,
                    () -> customerDisableUseCase.disableById(idInexistente));

            assertEquals("exception.resource.not-found.customer", excecao.getMessage());
            assertEquals(idInexistente.toString(), excecao.getValue());
            verify(customerQueryOutputPort, times(1)).findByIdAndActiveTrue(idInexistente);
        }
    }
}
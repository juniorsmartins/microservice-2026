package backend.finance.application.usecases;

import backend.finance.application.dtos.CustomerDto;
import backend.finance.application.dtos.RoleDto;
import backend.finance.application.exceptions.http404.CustomerNotFoundCustomException;
import backend.finance.application.mappers.CustomerMapperImpl;
import backend.finance.application.mappers.RoleMapperImpl;
import backend.finance.application.mappers.UserMapperImpl;
import backend.finance.application.ports.output.CustomerQueryOutputPort;
import backend.finance.application.ports.output.CustomerSaveOutputPort;
import backend.finance.application.validation.CustomerValidation;
import backend.finance.application.validation.RoleValidation;
import backend.finance.application.validation.UserValidation;
import backend.finance.enterprise.enums.RoleEnum;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static backend.finance.application.utils.CustomerTestFactory.buildDto;
import static backend.finance.application.utils.CustomerTestFactory.defaultRequest;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@Tag("Customer")
@Tag("UseCase")
class CustomerUpdateUseCaseTest {

    @Mock
    private CustomerQueryOutputPort customerQueryOutputPort;

    @Mock
    private CustomerSaveOutputPort customerSaveOutputPort;

    @Mock
    private CustomerValidation customerValidation;

    @Mock
    private UserValidation userValidation;

    @Mock
    private RoleValidation roleValidation;

    @InjectMocks
    private CustomerUpdateUseCase customerUpdateUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        var roleMapper = new RoleMapperImpl();
        var userMapper = new UserMapperImpl(roleMapper);
        var customerMapper = new CustomerMapperImpl(userMapper);

        customerUpdateUseCase = new CustomerUpdateUseCase(customerQueryOutputPort, customerSaveOutputPort,
                customerValidation, userValidation, roleValidation, customerMapper);
    }

    @Nested
    @Order(1)
    @DisplayName("Update - casos válidos.")
    @Tag("UpdateValid")
    class UpdateValid {

        @Test
        @DisplayName("Deve atualizar customer completo com sucesso.")
        void deveAtualizarComSucesso() {
            var request = defaultRequest();
            var dto = buildDto("username_teste55", "password_teste55",
                    RoleEnum.ROLE_CUSTOMER, "name_teste55", "email_teste55@yahoo.com");
            when(customerQueryOutputPort.findActiveById(any())).thenReturn(Optional.of(dto));

            doNothing().when(customerValidation).checkDuplicateEmail(any(), anyString());
            doNothing().when(userValidation).checkDuplicateUsername(any(), anyString());
            when(roleValidation.getOrCreateRole(any()))
                    .thenReturn(new RoleDto(UUID.randomUUID(), RoleEnum.ROLE_CUSTOMER.name()));

            when(customerSaveOutputPort.save(any(CustomerDto.class))).thenReturn(dto);
            when(customerSaveOutputPort.save(any(CustomerDto.class)))
                    .thenAnswer(i -> {
                        CustomerDto customerDto = i.getArgument(0);
                        return customerDto;
                    });

            customerUpdateUseCase.update(dto.id(), request);

            ArgumentCaptor<CustomerDto> captor = ArgumentCaptor.forClass(CustomerDto.class);
            verify(customerSaveOutputPort, times(1)).save(captor.capture());
            var atualizado = captor.getValue();
            assertAll("DTO salvo deve estar atualizado",
                    () -> assertEquals(request.name(), atualizado.name()),
                    () -> assertNotEquals(dto.name(), atualizado.name()),
                    () -> assertEquals(request.email(), atualizado.email()),
                    () -> assertNotEquals(dto.email(), atualizado.email()),
                    () -> assertTrue(atualizado.active()),
                    () -> assertEquals(request.user().username(), atualizado.user().username()),
                    () -> assertNotEquals(dto.user().username(), atualizado.user().username()),
                    () -> assertEquals(request.user().password(), atualizado.user().password()),
                    () -> assertNotEquals(dto.user().password(), atualizado.user().password()),
                    () -> assertTrue(atualizado.user().active()),
                    () -> assertEquals(request.user().role(), atualizado.user().role().name())
            );
        }
    }

    @Nested
    @Order(2)
    @DisplayName("Update - casos inválidos.")
    @Tag("UpdateInvalid")
    class UpdateInvalid {

        @Test
        @DisplayName("Deve lançar exceção quando atualizar customer inexistente.")
        void deveLancarExcecaoQuandoNaoEncontrarCustomer() {
            var idInexistente = UUID.randomUUID();
            var request = defaultRequest();
            when(customerQueryOutputPort.findActiveById(any())).thenReturn(Optional.empty());

            var excecao = assertThrows(CustomerNotFoundCustomException.class,
                    () -> customerUpdateUseCase.update(idInexistente, request));

            assertEquals("exception.resource.not-found.customer", excecao.getMessage());
            assertEquals(idInexistente.toString(), excecao.getValue());
            verify(customerQueryOutputPort, times(1)).findActiveById(idInexistente);
        }
    }
}
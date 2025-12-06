package backend.finance.application.usecases;

import backend.finance.application.dtos.CustomerDto;
import backend.finance.application.dtos.RoleDto;
import backend.finance.application.dtos.UserDto;
import backend.finance.application.mappers.CustomerMapperImpl;
import backend.finance.application.mappers.RoleMapperImpl;
import backend.finance.application.mappers.UserMapperImpl;
import backend.finance.application.ports.output.CustomerEventPublisherOutputPort;
import backend.finance.application.ports.output.CustomerSaveOutputPort;
import backend.finance.application.Utils.CustomerTestFactory;
import backend.finance.application.validation.CustomerValidation;
import backend.finance.application.validation.RoleValidation;
import backend.finance.application.validation.UserValidation;
import backend.finance.enterprise.enums.RoleEnum;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@Tag("Customer")
@Tag("UseCase")
class CustomerCreateUseCaseTest {

    @Mock
    private CustomerSaveOutputPort customerSaveOutputPort;

    @Mock
    private CustomerValidation customerValidation;

    @Mock
    private UserValidation userValidation;

    @Mock
    private RoleValidation roleValidation;

    @Mock
    private CustomerEventPublisherOutputPort customerEventPublisherOutputPort;

    @InjectMocks
    private CustomerCreateUseCase customerCreateUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        var roleMapper = new RoleMapperImpl();
        var userMapper = new UserMapperImpl(roleMapper);
        var customerMapper = new CustomerMapperImpl(userMapper);

        customerCreateUseCase = new CustomerCreateUseCase(customerSaveOutputPort, customerValidation, userValidation,
                roleValidation, customerEventPublisherOutputPort, customerMapper);
    }

    @Nested
    @Order(1)
    @DisplayName("Create - casos válidos.")
    @Tag("CreateValid")
    class CreateValid {

        @Test
        @DisplayName("Deve criar customer padrão.")
        void deveCriarCustomer() {
            var request = CustomerTestFactory.defaultRequest();
            doNothing().when(customerValidation).checkDuplicateEmail(any(), anyString());
            doNothing().when(userValidation).checkDuplicateUsername(any(), anyString());

            when(roleValidation.getOrCreateRole(any()))
                    .thenReturn(new RoleDto(UUID.randomUUID(), RoleEnum.ROLE_CUSTOMER.name()));

            when(customerSaveOutputPort.save(any(CustomerDto.class)))
                    .thenAnswer(i -> {
                        CustomerDto dto = i.getArgument(0);
                        var userDto = new UserDto(UUID.randomUUID(), dto.user().username(), dto.user().password(), dto.user().role(), dto.user().active());
                        return new CustomerDto(UUID.randomUUID(), dto.name(), dto.email(), userDto, dto.active());
                    });

            var created = customerCreateUseCase.create(request);

            assertAll("Cliente criado corretamente.",
                    () -> assertNotNull(created.id()),
                    () -> assertEquals(request.name(), created.name()),
                    () -> assertEquals(request.email(), created.email()),
                    () -> assertTrue(created.active()),
                    () -> assertNotNull(created.user().id()),
                    () -> assertEquals(request.user().username(), created.user().username()),
                    () -> assertTrue(created.user().active())
            );
        }
    }
}
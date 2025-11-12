package backend.finance.enterprise.entities;

import backend.finance.enterprise.enums.RoleEnum;
import org.junit.jupiter.api.*;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@Tag("Permissão")
class PermissaoTest {

    @Nested
    @Order(2)
    @DisplayName("PermissaoValid")
    @Tag("PermissaoValid")
    class PermissaoValid {

        @Test
        @DisplayName("Deve criar permissão padrão.")
        void deveCriarPermissao() {
            var permissao = Permissao.create(UUID.randomUUID(), RoleEnum.ROLE_ADMIN);
            assertNotNull(permissao.getId());
            assertEquals(RoleEnum.ROLE_ADMIN, permissao.getName());
        }
    }
}
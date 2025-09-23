package backend.finance.api_account.infrastructure.daos;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity // Indica que esta classe é uma entidade JPA
@Table(name = "clients") // Mapeia a entidade para a tabela "usuarios" no banco de dados
@AllArgsConstructor // Gera um construtor com todos os campos como parâmetros
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@EqualsAndHashCode(of = {"id"}) // Gera métodos equals e hashCode baseados no campo "id"
public final class CustomerJpa {

    @Id // Indica que este campo é a chave primária da entidade
    private UUID id;

    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;
}

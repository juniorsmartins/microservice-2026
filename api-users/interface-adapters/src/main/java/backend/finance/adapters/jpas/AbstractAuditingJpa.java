package backend.finance.adapters.jpas;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime;

@MappedSuperclass // Anotação de Herança para entidades JPA. Indica que esta classe não é uma entidade por si só, mas suas propriedades serão herdadas por entidades filhas.
@EntityListeners(AuditingEntityListener.class) // Especifica que a classe usará o AuditingEntityListener para preencher automaticamente os campos de auditoria.
@Getter
@Setter
public abstract class AbstractAuditingJpa {

    @Column(name = "created_by", updatable = false)
    private String createdBy;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private OffsetDateTime createdDate;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private OffsetDateTime lastModifiedDate;
}

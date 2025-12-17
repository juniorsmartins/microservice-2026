package backend.finance.adapters.jpas.auditoria;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime;

@Audited
@MappedSuperclass // Anotação de Herança para entidades JPA. Indica que esta classe não é uma entidade por si só, mas suas propriedades serão herdadas por entidades filhas.
@EntityListeners(AuditingEntityListener.class) // Especifica que a classe usará o AuditingEntityListener para preencher automaticamente os campos de auditoria.
@Getter
@Setter
public abstract class AbstractAuditingJpa {

    @Column(name = "created_by", updatable = false)
    private String createdBy; // Este campo é usado para registrar quem criou a entidade.

    @Column(name = "last_modified_by")
    private String lastModifiedBy; // Usado para identificar quem modificou a entidade pela última vez.

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private OffsetDateTime createdDate; // Para registrar a data e a hora em que a entidade foi criada.

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private OffsetDateTime lastModifiedDate; // Usado para registrar quando uma entidade foi modificada pela última vez.
}

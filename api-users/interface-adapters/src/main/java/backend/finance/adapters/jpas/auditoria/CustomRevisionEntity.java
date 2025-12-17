package backend.finance.adapters.jpas.auditoria;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

import java.time.Instant;

@Entity
@Table(name = "revision_info")
@RevisionEntity // Indica ao Envers que esta é a classe de controle de revisões
@Getter
@Setter
public class CustomRevisionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @RevisionNumber
    @Column(name = "revision_id")
    private Long revisionId;

    @RevisionTimestamp
    @Column(name = "revision_timestamp")
    private Instant revisionTimestamp;
}

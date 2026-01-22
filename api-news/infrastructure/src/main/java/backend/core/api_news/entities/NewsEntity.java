package backend.core.api_news.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "news")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@ToString
public final class NewsEntity extends AbstractAuditingJpa implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public NewsEntity(UUID id, String hat, String title, String thinLine, String text, String author, String font,
                      String createdBy, String lastModifiedBy, OffsetDateTime createdDate, OffsetDateTime lastModifiedDate) {
        this.id = id;
        this.hat = hat;
        this.title = title;
        this.thinLine = thinLine;
        this.text = text;
        this.author = author;
        this.font = font;
        this.setCreatedBy(createdBy);
        this.setLastModifiedBy(lastModifiedBy);
        this.setCreatedDate(createdDate);
        this.setLastModifiedDate(lastModifiedDate);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // TODO - definir tamanhos das strings nas colunas abaixo
    @Column(name = "hat", nullable = false)
    private String hat;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "thin_line", nullable = false)
    private String thinLine;

    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "font", nullable = false)
    private String font;
}

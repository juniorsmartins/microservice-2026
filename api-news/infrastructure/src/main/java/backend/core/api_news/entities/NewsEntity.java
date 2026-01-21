package backend.core.api_news.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
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

package backend.core.api_news.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "news")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public final class NewsEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "hat", nullable = false)
    private String hat;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "thin_line", nullable = false)
    private String thinLine;

    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "font", nullable = false)
    private String font;
}

package oudedong.project.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Article {
    
    @Id
    @Column(name = "id", unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name="title", nullable = false)
    private String title;

    @Column(name="body", nullable = false)
    private String body;

    @Column(name="userId", nullable = false)
    private Long userId;

    @Column(name="createdDate", nullable = false)
    private LocalDateTime created;

    @Column(name="lastModified", nullable = true)
    private LocalDateTime lastModified;
}

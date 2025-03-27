package oudedong.project.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import oudedong.project.domain.Article;

@Repository
public interface ArticleRepo extends JpaRepository<Article, Long>{
    Optional<Article> findByUserIdAndTitle(Long userid, String title);
    List<Article> findAllByCreated(LocalDateTime created, Pageable page);
    List<Article> findAllByTitle(String title, Pageable page);
    List<Article> findAllByUserId(Long userid, Pageable page);
    Page<Article> findAll(Pageable page);
}

package oudedong.project.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import oudedong.project.domain.Article;
//스프링부트는 트랜젝션이 끝나면 엔티티매니져를 닫아버림->엔티티들이 준영속 상태가됨!
@Repository
public interface ArticleRepo extends JpaRepository<Article, Long>{
    Optional<Article> findByUserIdAndTitle(Long userid, String title);
    Page<Article> findAllByCreated(LocalDateTime created, Pageable page);
    Page<Article> findAllByTitle(String title, Pageable page);
    Page<Article> findAllByUserId(Long userid, Pageable page);
    Page<Article> findAll(Pageable page);

    @Query("SELECT new Article(a.title, a.userId, a.created, a.lastModified) " +
           "FROM Article a WHERE a.id = :articleId")
    Optional<Article> findEssentialById(@Param("articleId") Long articleId);
}

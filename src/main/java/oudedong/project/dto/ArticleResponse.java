package oudedong.project.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import oudedong.project.domain.Article;

@Getter
@Builder
@AllArgsConstructor
public class ArticleResponse {
    
    private String title;
    private String body;
    private String writer;
    private LocalDateTime created;
    private LocalDateTime modifiedTime;
    private Long articleId;

    public ArticleResponse(Article article){
        title = article.getTitle();
        body = article.getBody();
        created = article.getCreated();
        modifiedTime = article.getLastModified();
        articleId = article.getId();
    }
    public ArticleResponse setWriter(String writer){
        this.writer = writer;
        return this;
    }
}

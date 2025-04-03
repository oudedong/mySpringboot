package oudedong.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ArticleRequest {
    
    private Long articleId;
    private String title;
    private String body;
    private Long userId;

    public static ArticleRequest OnlyId(Long id){
        return new ArticleRequest(id, null, null, null);
    }
}

package oudedong.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticleRequest {
    
    private Long articleId;
    private String title;
    private String body;
    private String username;

    public static ArticleRequest OnlyId(Long id){
        return new ArticleRequest(id, null, null, null);
    }
}

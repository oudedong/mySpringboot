package oudedong.project.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import oudedong.project.dto.ArticleRequest;
import oudedong.project.dto.ArticleResponse;
import oudedong.project.service.ArticleService;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api")
public class ArticleApiController {

    private final ArticleService articleService;

    @GetMapping("/articles/{page}")
    public ResponseEntity<List<ArticleResponse>> getPage(@PathVariable int page) {
        
        List<ArticleResponse> articles =  articleService.requestArticle(null, page);

        return ResponseEntity.ok().body(articles);
    }
    
    @GetMapping("/article/{id}")
    public ResponseEntity<ArticleResponse> getArticle(@PathVariable Long id){

        ArticleRequest request = ArticleRequest.builder().articleId(id).build();
        ArticleResponse article = articleService
            .requestArticle(request, 1)
            .get(0);

        return ResponseEntity.ok().body(article);
    }
    
    @PostMapping("/articles")
    public ResponseEntity<ArticleResponse> createArticle(@RequestBody ArticleRequest request) {
        
        ArticleResponse response = articleService.saveOrModifyArticle(request);

        return ResponseEntity.ok().body(response);
    }
    
}

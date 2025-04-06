package oudedong.project.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import oudedong.project.dto.ArticleRequest;
import oudedong.project.dto.ArticleResponse;
import oudedong.project.dto.UserDetail;
import oudedong.project.service.ArticleService;
import oudedong.project.service.UserService;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api")
public class ArticleApiController {

    private final ArticleService articleService;
    private final UserService userService;

    @GetMapping("/articles/{page}")
    public ResponseEntity<List<ArticleResponse>> getPage(@PathVariable("page") int page) {
        
        List<ArticleResponse> articles =  articleService.requestArticle(null, page);

        return ResponseEntity.ok().body(articles);
    }
    
    @GetMapping("/article/{id}")
    public ResponseEntity<ArticleResponse> getArticle(@PathVariable("id") Long id){

        ArticleRequest request = ArticleRequest.builder().articleId(id).build();
        ArticleResponse article = articleService
            .requestArticle(request, 1)
            .get(0);

        return ResponseEntity.ok().body(article);
    }
    
    @PostMapping("/articles")
    public ResponseEntity<ArticleResponse> createArticle(@RequestBody ArticleRequest request) {
        
        UserDetail user = userService.getCurrentUser();
        if(user == null){
            //System.out.println("here");
            throw new AccessDeniedException("require login");
        }
        System.out.println("name: " + user.getUsername());
        request.setUsername(user.getUsername());
        System.out.println("id: " + user.getId());
        ArticleResponse response = articleService.saveOrModifyArticle(request);

        return ResponseEntity.ok().body(response);
    }
    
    @DeleteMapping("/article/{id}")
    public ResponseEntity<ArticleResponse> deleteArticle(@PathVariable(name="id") Long id) {
        
        ArticleRequest request = new ArticleRequest(id, null, null, null);
        ArticleResponse response = articleService.deleteArticle(request);

        return ResponseEntity.ok().body(response);
    }
}

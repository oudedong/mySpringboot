package oudedong.project.service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import oudedong.project.domain.Article;
import oudedong.project.domain.User;
import oudedong.project.dto.ArticleRequest;
import oudedong.project.dto.ArticleResponse;
import oudedong.project.repository.ArticleRepo;
import oudedong.project.repository.UserRepo;

@Service
@RequiredArgsConstructor
public class ArticleService {
    
    private final ArticleRepo articleRepo;
    private final UserRepo userRepo;
    private final Supplier<IllegalArgumentException> articleExep = ()->new IllegalArgumentException("can't find article");
    private final Supplier<IllegalArgumentException> userExep = ()->new IllegalArgumentException("can't find user");
    private final int pageSize = 10;

    @Transactional
    public ArticleResponse saveOrModifyArticle(ArticleRequest request){
        
        Article article = articleRepo.findByUseridAndTitle(request.getUserId(), request.getTitle()).orElse(null);
        String writerName = findArticleWriter(request.getUserId());

        if(article == null){
            article = Article.builder()
                .userId(request.getUserId())
                .title(request.getTitle())
                .body(request.getBody())
                .created(LocalDateTime.now())
                .build();
            
            ArticleResponse response = new ArticleResponse(articleRepo.save(article));

            response.setWriter(writerName);
            return response;
        }
        article.setTitle(request.getTitle());
        article.setBody(request.getBody());
        article.setLastModified(LocalDateTime.now());

        return new ArticleResponse(article).setWriter(writerName);
    }
    public ArticleResponse requestPreview(Long ariticleId){
        
        Article article = articleRepo.findById(ariticleId).orElseThrow(articleExep);
        ArticleResponse response = ArticleResponse.builder()
            .title(article.getTitle())
            .writer(findArticleWriter(article.getUserId()))
            .created(article.getCreated())
            .build();

        return response;
    }
    public List<ArticleResponse> requestArticle(ArticleRequest request, int page){

        List<Article> found = new LinkedList<>();

        if(request.getArticleId() != null){
            found.add(articleRepo.findById(request.getArticleId()).orElseThrow(articleExep));
        }
        if(request.getUserId() != null){
            found.addAll(articleRepo.findAllByUserid(request.getUserId(), PageRequest.of(page, pageSize)));
        }
        if(request.getTitle() != null){
            found.addAll(articleRepo.findAllByTitle(request.getTitle(), PageRequest.of(page, pageSize)));
        }
        return found.stream().map((article)->
            new ArticleResponse(article).setWriter(findArticleWriter(article.getUserId()))
        ).toList();
    }
    @Transactional
    public ArticleResponse deleteArticle(ArticleRequest request){
        Article article = articleRepo.findById(request.getArticleId()).orElseThrow(articleExep);
        ArticleResponse response = new ArticleResponse(article);

        response.setWriter(findArticleWriter(article.getUserId()));
        articleRepo.delete(article);
        return response;
    }


    private String findArticleWriter(Long userid){
        User writer = userRepo.findById(userid).orElseThrow(userExep);
        return writer.getUsername();
    }

}

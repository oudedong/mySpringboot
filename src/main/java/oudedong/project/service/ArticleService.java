package oudedong.project.service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import oudedong.project.domain.Article;
import oudedong.project.domain.User;
import oudedong.project.dto.ArticleRequest;
import oudedong.project.dto.ArticleResponse;
import oudedong.project.dto.UserDetail;
import oudedong.project.repository.ArticleRepo;
import oudedong.project.repository.UserRepo;
import oudedong.project.vo.AuthorityType;

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
        
        String writerName = request.getUsername();
        User writer = userRepo.findByUsername(writerName).orElseThrow(userExep);
        Article article = null;

        if(request.getArticleId() == null){
            article = Article.builder()
                .userId(writer.getId())
                .title(request.getTitle())
                .body(request.getBody())
                .created(LocalDateTime.now())
                .build();
            
            ArticleResponse response = new ArticleResponse(articleRepo.save(article));

            response.setWriter(writerName);
            return response;
        }
        article = articleRepo.findById(request.getArticleId()).orElse(null);
        if(!checkPermisson(article)){
            System.out.println("not owner of this article");
            throw new AccessDeniedException("not owner of this article");
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

        if(request != null){
            if(request.getArticleId() != null){
                found.add(articleRepo.findById(request.getArticleId()).orElseThrow(articleExep));
            }
            if(request.getUsername() != null){
                User user = userRepo.findByUsername(request.getUsername()).orElseThrow(userExep);
                found.addAll(articleRepo.findAllByUserId(user.getId(), PageRequest.of(page, pageSize)).toList());
            }
            if(request.getTitle() != null){
                found.addAll(articleRepo.findAllByTitle(request.getTitle(), PageRequest.of(page, pageSize)).toList());
            }
        }else{
            found.addAll(articleRepo.findAll(PageRequest.of(page, pageSize)).getContent());
        }
        return found.stream().map((article)->
            new ArticleResponse(article).setWriter(findArticleWriter(article.getUserId()))
        ).toList();
    }
    @Transactional
    public ArticleResponse deleteArticle(ArticleRequest request){
        
        Article article = articleRepo.findById(request.getArticleId()).orElseThrow(articleExep);
        ArticleResponse response = new ArticleResponse(article);

        if(!checkPermisson(article)){
            throw new AccessDeniedException("not owner of this article");
        }
        response.setWriter(findArticleWriter(article.getUserId()));
        articleRepo.delete(article);
        return response;
    }
    public boolean checkPermisson(Article article){
        
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<GrantedAuthority> authority = new LinkedList<>(SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        UserDetail user = null;
        
        if(principal == null){
            System.out.println("no3");
            return false;
        }
        user = (UserDetail)principal;

        for(GrantedAuthority auth : authority){
            System.out.println("auth: "+auth.getAuthority());
            if(auth.getAuthority().equals(AuthorityType.ROLE_ADMIN.toString())){
                return true;
            }
            if(auth.getAuthority().equals(AuthorityType.ROLE_VISITOR.toString())){
                return false;
            }
            if(auth.getAuthority().equals(AuthorityType.ROLE_USER.toString())){
                if(article.getUserId() != user.getId()){
                    return false;
                }
                return true;
            }
        }
        return false;
    }
    public boolean checkPermisson(Long id){
        
        Article article = articleRepo.findEssentialById(id).orElseThrow(()->new NoSuchElementException("cant find article"));

        return checkPermisson(article);
    }
    public Long getPageCnt(){
        return Long.valueOf(Double.valueOf(Math.ceil((double)articleRepo.count()/pageSize)).intValue());
    }
    private String findArticleWriter(Long userid){
        User writer = userRepo.findById(userid).orElseThrow(userExep);
        return writer.getUsername();
    }

}

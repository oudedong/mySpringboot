package oudedong.project.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import lombok.RequiredArgsConstructor;
import oudedong.project.dto.ArticleRequest;
import oudedong.project.dto.ArticleResponse;
import oudedong.project.dto.UserDetail;
import oudedong.project.service.ArticleService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequiredArgsConstructor
@RequestMapping("/main")
public class AriticleViewController {
    
    private final ArticleService articleService;

    @GetMapping("/articles/{page}")
    public String getArticlePage(@PathVariable("page") Long page, Model model) {

        List<ArticleResponse> articles = articleService.requestArticle(null, page.intValue());
        List<Long> pageNums = new ArrayList<>();

        Long total = articleService.getPageCnt();
        long left = page-1, right = page+1;

        pageNums.add(page);
        while(pageNums.size() < 10 && pageNums.size() < total){
            if(left > 0){
                pageNums.addFirst(left);
                left--;
            }
            if(right <= total){
                pageNums.addLast(right);
                right++;
            }
        }
        model.addAttribute("articles", articles);
        model.addAttribute("currentpage", page);
        model.addAttribute("allPages", pageNums);
        model.addAttribute("currentUser", getSessionPrincipal());
        return "articles";
    }
    @GetMapping("/article/{id}")
    public String getArticle(@PathVariable("id") Long id, Model model){

        ArticleResponse article = articleService.requestArticle(ArticleRequest.OnlyId(id), 0).get(0);
        boolean isOwner = articleService.checkPermisson(id);

        model.addAttribute("article", article);
        model.addAttribute("isOwner", isOwner);
        return "article";
    }
    @GetMapping("/article/writting")
    public String getWrittingPage(@RequestParam(value = "id", required = false) Long writtingId, Model model){
        model.addAttribute("id", writtingId);
        return "writting";
    }
    private String getSessionPrincipal(){
        try{
            UserDetail user = (UserDetail)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return user.getUsername();
        }catch(Exception e){
            return "";
        }
    }
}

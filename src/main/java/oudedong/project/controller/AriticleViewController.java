package oudedong.project.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import lombok.RequiredArgsConstructor;
import oudedong.project.dto.ArticleRequest;
import oudedong.project.dto.ArticleResponse;
import oudedong.project.service.ArticleService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequiredArgsConstructor
@RequestMapping("/main")
public class AriticleViewController {
    
    private final ArticleService articleService;

    @GetMapping("/articles/{page}")
    public String getArticlePage(@PathVariable("page") Long page, Model model) {

        System.out.println("ok");

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
        return "articles";
    }
    @GetMapping("/article/{id}")
    public String getArticle(@PathVariable("id") Long id, Model model){

        ArticleResponse article = articleService.requestArticle(ArticleRequest.OnlyId(id), 0).get(0);

        model.addAttribute("article", article);
        return "article";
    }
    @GetMapping("/article/writting")
    public String getWrittingPage(@RequestBody(required = false) ArticleRequest request, Model model){

        boolean isEdit;

        if(request==null){
            isEdit = false;
        }
        else{
            isEdit = true;
            model.addAttribute("article", articleService.requestArticle(ArticleRequest.OnlyId(request.getArticleId()), 0).get(0));
        }
        model.addAttribute("isEdit", isEdit);
        return "writting";
    }
}

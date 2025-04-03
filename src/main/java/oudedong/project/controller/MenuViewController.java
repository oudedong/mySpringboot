package oudedong.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import oudedong.project.service.ArticleService;


@Controller
@RequiredArgsConstructor
public class MenuViewController {
    
    private final ArticleService articleService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }
    @GetMapping("/register")
    public String register() {
        return "register";
    }
    @GetMapping("/main")
    public String main() {
        return "main";
    }
    @GetMapping("/console")
    public String console() {
        return "console";
    }
}

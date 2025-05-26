package ntu.hongdta_64130758.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import ntu.hongdta_64130758.models.Category;
import ntu.hongdta_64130758.models.Post;
import ntu.hongdta_64130758.repositories.CategoryRepository;
import ntu.hongdta_64130758.security.CustomUserDetails;
import ntu.hongdta_64130758.services.implement.PostService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Controller
public class HomeController {

    @Autowired
    PostService postService;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/")
    public String home(Model model) {
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String fullName = "Khách";
        if (auth.getPrincipal() instanceof CustomUserDetails userDetails) {
            fullName = userDetails.getFullName();
        }

        model.addAttribute("full_name", fullName);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("posts", postService.getAllPosts());

        return "home";
    }
    
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
}

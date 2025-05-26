package ntu.hongdta_64130758.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import ntu.hongdta_64130758.models.Category;
import ntu.hongdta_64130758.models.Post;
import ntu.hongdta_64130758.repositories.CategoryRepository;
import ntu.hongdta_64130758.services.implement.PostService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class HomeController {

    @Autowired
    PostService postService;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/")
    public String home(Model model) {
        List<Category> categories = categoryRepository.findAll(); 
        model.addAttribute("categories", categories);

        List<Post> posts = postService.findAll(); // Lấy danh sách bài viết
        model.addAttribute("posts", posts);

        model.addAttribute("full_name", "Nguyễn Văn A"); // Thêm tên người dùng (hoặc từ session)

        return "home"; // trả về trang home.html
    }
}

package ntu.hongdta_64130758.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import ntu.hongdta_64130758.models.Category;
import ntu.hongdta_64130758.repositories.CategoryRepository;
import ntu.hongdta_64130758.services.implement.CategoryService;
import ntu.hongdta_64130758.services.interf.ICategoryService;

@Controller
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    CategoryRepository categoryRepository;

    @PostMapping("/categories/create")
    public String createCategory(HttpServletRequest request) {
        String name = request.getParameter("newCategoryName");
        categoryService.saveCategory(name);
        return "redirect:/posts/create"; 
    }
}

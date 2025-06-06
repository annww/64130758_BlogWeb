package ntu.hongdta_64130758.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import ntu.hongdta_64130758.models.Category;
import ntu.hongdta_64130758.repositories.CategoryRepository;
import ntu.hongdta_64130758.services.implement.CategoryService;
@RequestMapping("/categories")
@Controller
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    CategoryRepository categoryRepository;

    @PostMapping("/create")
    public String createCategory(
            @ModelAttribute Category category,
            @RequestHeader(value = "referer", required = false) String referer,
            RedirectAttributes redirectAttributes) {

        categoryService.save(category);
        redirectAttributes.addFlashAttribute("successMessage", "Thêm danh mục thành công!");
        return "redirect:" + (referer != null ? referer : "/categories/manage");
    }

    @PostMapping("/update")
    public String updateAllCategories(@ModelAttribute("category") Category category) {
    	categoryRepository.save(category);
        return "redirect:/";
    }
    
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy danh mục có ID: " + id));
        model.addAttribute("category", category);
        return "categories/edit"; 
    }
    
    @PostMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long id) {
        categoryRepository.deleteById(id);
        return "redirect:/"; 
    }

    @GetMapping("/manage")
    public String showManageCategories(Model model) {
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("category", new Category());
        return "categories/manage";  
    }
    @PostMapping("/manage")
    public String updateCategories(@ModelAttribute("categories") List<Category> categories) {
        for (Category cat : categories) {
            categoryService.save(cat);
        }
        return "redirect:/categories/manage";
    }
}

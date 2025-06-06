package ntu.hongdta_64130758.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import ntu.hongdta_64130758.models.Category;
import ntu.hongdta_64130758.repositories.CategoryRepository;
import ntu.hongdta_64130758.services.implement.CategoryService;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/manage")
    public String showManageCategories(Model model) {
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("category", new Category()); 
        return "categories/manage";
    }

    @PostMapping("/create")
    public String createCategory(@ModelAttribute("category") Category category, Model model) {
        if(category.getName() == null || category.getName().trim().isEmpty()) {
            model.addAttribute("errorMessage", "Tên danh mục không được để trống");
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("openCategoryModal", true); 
            return "categories/manage"; 
        }

        categoryService.save(category);
        model.addAttribute("successMessage", "Đã thêm danh mục mới");
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("category", new Category()); 
        return "categories/manage";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy danh mục có ID: " + id));
        model.addAttribute("category", category);
        return "categories/edit"; 
    }

    @PostMapping("/update")
    public String updateCategory(@ModelAttribute("category") Category category, Model model) {
        if(category.getName() == null || category.getName().trim().isEmpty()) {
            model.addAttribute("errorMessage", "Tên danh mục không được để trống");
            return "categories/edit";
        }
        categoryService.save(category);
        return "redirect:/categories/manage"; 
    }

    @PostMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long id) {
        categoryRepository.deleteById(id);
        return "redirect:/categories/manage"; 
    }
}
package ntu.hongdta_64130758.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import ntu.hongdta_64130758.models.Category;
import ntu.hongdta_64130758.repositories.CategoryRepository;
import ntu.hongdta_64130758.services.interf.ICategoryService;

@Controller
@CrossOrigin 
public class CategoryController {

    @Autowired
    ICategoryService categorySerice;

    @Autowired
    CategoryRepository categoryRepository;

    @PostMapping("/categories/create")
    @ResponseBody
    public ResponseEntity<?> createCategory(@RequestBody Map<String, String> data) {
        String name = data.get("name");

        if (name == null || name.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Tên không được rỗng");
        }

        Category category = new Category();
        category.setName(name);

        try {
            categoryRepository.save(category);
            return ResponseEntity.ok(category);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Lỗi server: " + e.getMessage());
        }
    }
}

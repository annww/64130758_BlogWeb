package ntu.hongdta_64130758.services.implement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ntu.hongdta_64130758.models.Category;
import ntu.hongdta_64130758.repositories.CategoryRepository;
import ntu.hongdta_64130758.services.interf.ICategoryService;

@Service
public class CategoryService implements ICategoryService {

	@Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
    
    public Category saveCategory(String name) {
        if (!categoryRepository.existsByName(name)) {
        	Category cat = new Category();
        	cat.setName(name);
            return categoryRepository.save(cat);
        }
        return null;
    }

}

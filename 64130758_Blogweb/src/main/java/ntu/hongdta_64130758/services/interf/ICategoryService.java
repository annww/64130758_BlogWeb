package ntu.hongdta_64130758.services.interf;

import java.util.List;

import ntu.hongdta_64130758.models.Category;

public interface ICategoryService {
	List<Category> getAllCategories();
	List<Category> findAll();
	Category findById(Long id);
    void update(Category category);
    void updateAll(List<Category> categories);
    Category save(Category category);
}

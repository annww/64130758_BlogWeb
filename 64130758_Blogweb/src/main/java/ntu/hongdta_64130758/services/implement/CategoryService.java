package ntu.hongdta_64130758.services.implement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ntu.hongdta_64130758.models.Category;
import ntu.hongdta_64130758.models.Post;
import ntu.hongdta_64130758.repositories.CategoryRepository;
import ntu.hongdta_64130758.services.interf.ICategoryService;

@Service
public class CategoryService implements ICategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    public Category saveCategory(String name) {
        if (!categoryRepository.existsByName(name)) {
            Category cat = new Category();
            cat.setName(name);
            return categoryRepository.save(cat);
        }
        return null;
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category findById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Override
    public void update(Category category) {
        Category existing = categoryRepository.findById(category.getId()).orElse(null);
        if (existing != null) {
            existing.setName(category.getName());
            existing.setDescription(category.getDescription());
            categoryRepository.save(existing);
        }
    }

    @Override
    public void updateAll(List<Category> categories) {
        categoryRepository.saveAll(categories);
    }

    public void deleteCategoryById(Long categoryId) {
        List<Post> posts = postService.findByCategoryId(categoryId);

        for (Post post : posts) {
            commentService.deleteCommentsByPostId(post.getId()); 
        }

        for (Post post : posts) {
            postService.deletePostById(post.getId());
        }

        categoryRepository.deleteById(categoryId);
    }
}

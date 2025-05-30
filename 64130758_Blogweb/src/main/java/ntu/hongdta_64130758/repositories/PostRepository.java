package ntu.hongdta_64130758.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ntu.hongdta_64130758.models.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
	List<Post> findByTitleContainingIgnoreCase(String keyword);
	List<Post> findAllByOrderByCreatedAtDesc();
	List<Post> findAllByOrderByCreatedAtAsc();
	List<Post> findAllByOrderByTitleAsc();
    List<Post> findAllByOrderByTitleDesc();
    List<Post> findByCategoryId(Long categoryId);
	List<Post> findByTitleContainingAndCategoryId(String keyword, Long categoryId);
	List<Post> findByTitleContaining(String keyword);

}
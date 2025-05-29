package ntu.hongdta_64130758.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ntu.hongdta_64130758.models.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
	boolean existsByName(String name);
}
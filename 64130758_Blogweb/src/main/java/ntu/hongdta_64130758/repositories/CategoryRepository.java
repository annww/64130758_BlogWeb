package ntu.hongdta_64130758.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ntu.hongdta_64130758.models.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
	
}

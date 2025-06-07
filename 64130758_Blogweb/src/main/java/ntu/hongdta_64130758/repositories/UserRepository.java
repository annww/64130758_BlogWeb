package ntu.hongdta_64130758.repositories;

import java.util.Optional;
import ntu.hongdta_64130758.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUsername(String username);
}

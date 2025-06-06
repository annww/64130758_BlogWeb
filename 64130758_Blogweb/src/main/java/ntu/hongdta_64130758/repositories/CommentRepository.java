package ntu.hongdta_64130758.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ntu.hongdta_64130758.models.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	List<Comment> findByPostId(Long postId);
    List<Comment> findByPostIdAndParentIsNull(Long postId);
}

package ntu.hongdta_64130758.services.interf;

import java.util.List;

import ntu.hongdta_64130758.models.Comment;

public interface ICommentService {
	List<Comment> getCommentsByPostId(Long postId);
    void saveComment(Comment comment);
    Comment findById(Long id);
    void deleteComment(Long id);
}

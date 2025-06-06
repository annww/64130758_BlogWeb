package ntu.hongdta_64130758.services.interf;

import java.util.List;
import ntu.hongdta_64130758.models.Comment;

public interface ICommentService {
    List<Comment> getParentCommentsByPostId(Long postId);
    List<Comment> getCommentsByPostId(Long postId);
    Comment findById(Long id);
    void deleteComment(Long id);
    Comment getCommentById(Long id);
    Comment saveComment(Comment comment);
}

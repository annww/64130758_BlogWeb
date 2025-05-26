package ntu.hongdta_64130758.services.implement;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ntu.hongdta_64130758.models.Comment;
import ntu.hongdta_64130758.repositories.CommentRepository;
import ntu.hongdta_64130758.services.interf.ICommentService;

@Service
public class CommentService implements ICommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    @Override
    public void saveComment(Comment comment) {
        comment.setCreatedAt(LocalDateTime.now());
        commentRepository.save(comment);
    }
    @Override
    public Comment findById(Long id) {
        return commentRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

}

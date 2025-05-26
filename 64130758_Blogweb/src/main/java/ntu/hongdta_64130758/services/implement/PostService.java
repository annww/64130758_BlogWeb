package ntu.hongdta_64130758.services.implement;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ntu.hongdta_64130758.models.Post;
import ntu.hongdta_64130758.repositories.PostRepository;
import ntu.hongdta_64130758.services.interf.IPostService;

@Service
public class PostService implements IPostService {

    @Autowired
    private PostRepository postRepository;

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public Post getPostById(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));
    }
    
    @Override
    public Post savePost(Post post) {
        if (post == null) {
            throw new IllegalArgumentException("Post không được null");
        }
        post.setCreatedAt(LocalDateTime.now());
        return postRepository.save(post);
    }
    
    @Override
    public Post findById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with id " + id));
    }

}
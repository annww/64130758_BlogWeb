package ntu.hongdta_64130758.services.interf;

import java.util.List;

import ntu.hongdta_64130758.models.Post;

public interface IPostService {
    List<Post> getAllPosts();
    Post getPostById(Long id);
    Post createPost(Post post);
}
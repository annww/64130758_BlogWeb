package ntu.hongdta_64130758.services.implement;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Comparator;

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
    
    public List<Post> getPostsByCategoryId(Long categoryId) {
        return postRepository.findByCategoryId(categoryId);
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
    
    public List<Post> findAllPostsSorted(String keyword, Long categoryId, String sort) {
        List<Post> posts;

        if (keyword != null && categoryId != null) {
            posts = postRepository.findByTitleContainingAndCategoryId(keyword, categoryId);
        } else if (keyword != null) {
            posts = postRepository.findByTitleContaining(keyword);
        } else if (categoryId != null) {
            posts = postRepository.findByCategoryId(categoryId);
        } else {
            posts = postRepository.findAll();
        }

        switch (sort) {
            case "newest":
                posts.sort(Comparator.comparing(Post::getCreatedAt).reversed());
                break;
            case "oldest":
                posts.sort(Comparator.comparing(Post::getCreatedAt));
                break;
            case "alphabet_asc":
                posts.sort(Comparator.comparing(Post::getTitle));
                break;
            case "alphabet_desc":
                posts.sort(Comparator.comparing(Post::getTitle).reversed());
                break;
            default:
                posts.sort(Comparator.comparing(Post::getCreatedAt).reversed());
                break;
        }

        return posts;
    }

    
    public List<Post> findPostsByCategoryAndKeyword(Long categoryId, String keyword, String sort) {
        List<Post> posts = postRepository.findAll();

        if (categoryId != null) {
            posts = posts.stream()
                         .filter(p -> p.getCategory() != null && categoryId.equals(p.getCategory().getId()))
                         .collect(Collectors.toList());
        }

        if (keyword != null && !keyword.isBlank()) {
            String lowerKeyword = keyword.toLowerCase();
            posts = posts.stream()
                         .filter(p -> {
                             String title = p.getTitle() != null ? p.getTitle().toLowerCase() : "";
                             String content = p.getContent() != null ? p.getContent().toLowerCase() : "";
                             return title.contains(lowerKeyword) || content.contains(lowerKeyword);
                         })
                         .collect(Collectors.toList());
        }

        Comparator<Post> comparator;
        switch (sort == null ? "" : sort) {
            case "newest":
                comparator = Comparator.comparing(Post::getCreatedAt, Comparator.nullsLast(Comparator.naturalOrder())).reversed();
                break;
            case "oldest":
                comparator = Comparator.comparing(Post::getCreatedAt, Comparator.nullsLast(Comparator.naturalOrder()));
                break;
            case "alphabet_asc":
                comparator = Comparator.comparing(Post::getTitle, Comparator.nullsLast(String::compareToIgnoreCase));
                break;
            case "alphabet_desc":
                comparator = Comparator.comparing(Post::getTitle, Comparator.nullsLast(String::compareToIgnoreCase)).reversed();
                break;
            default:
                comparator = Comparator.comparing(Post::getId);
        }

        return posts.stream()
                    .sorted(comparator)
                    .collect(Collectors.toList());
    }
    
    public List<Post> findByCategoryId(Long categoryId) {
        return postRepository.findByCategoryId(categoryId);
    }

    @Override
    public void deletePostById(Long id) {
        postRepository.deleteById(id);
    }

}

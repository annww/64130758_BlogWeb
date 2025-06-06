package ntu.hongdta_64130758.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; 
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import ntu.hongdta_64130758.models.Category;
import ntu.hongdta_64130758.models.Comment;
import ntu.hongdta_64130758.models.Post;
import ntu.hongdta_64130758.models.User;
import ntu.hongdta_64130758.repositories.PostRepository;
import ntu.hongdta_64130758.services.implement.CategoryService;
import ntu.hongdta_64130758.services.implement.CommentService;
import ntu.hongdta_64130758.services.implement.PostService;
import ntu.hongdta_64130758.services.implement.UserService;

@Controller
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private PostRepository postRepository;
    
    @GetMapping("")
    public String showPostList(
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) Long categoryId,
        @RequestParam(required = false) String sort,
        Model model) {

        if (sort == null) {
            sort = ""; 
        }

        List<Post> posts = postService.findAllPostsSorted(keyword, categoryId, sort);

        model.addAttribute("posts", posts);
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("sort", sort);
        model.addAttribute("categories", categoryService.getAllCategories());	
        return "posts/index"; 
    }

    @GetMapping("/{id}")
    public String showPostDetail(@PathVariable Long id, 
                                 Model model, 
                                 @AuthenticationPrincipal UserDetails userDetails) {
        Post post = postService.findById(id);

        List<Comment> parentComments = commentService.getParentCommentsByPostId(id);

        model.addAttribute("post", post);
        model.addAttribute("comments", parentComments);

        if (userDetails != null) {
            User currentUser = userService.findByUsername(userDetails.getUsername());
            model.addAttribute("currentUser", currentUser);
        }

        return "posts/detail";
    }

    
    @GetMapping("/edit/{id}")
    public String showEditPostForm(@PathVariable Long id, Model model) {
        Post post = postService.findById(id);
        if (post == null) {
            return "redirect:/posts";
        }

        model.addAttribute("post", post);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "posts/edit";
    }


    @PostMapping("/edit/{id}")
    public String updatePost(@PathVariable Long id, 
                             @ModelAttribute("post") Post updatedPost,
                             @AuthenticationPrincipal UserDetails userDetails) {
        Post existingPost = postService.findById(id);
        User currentUser = userService.findByUsername(userDetails.getUsername());

        if (!existingPost.getAuthor().getId().equals(currentUser.getId())) {
            return "redirect:/posts/" + id;
        }

        existingPost.setTitle(updatedPost.getTitle());
        existingPost.setContent(updatedPost.getContent());
        existingPost.setCategory(updatedPost.getCategory());

        postService.savePost(existingPost);
        return "redirect:/posts/" + id;
    }

    @PostMapping("/delete/{id}")
    public String deletePost(@PathVariable Long id, 
                             @AuthenticationPrincipal UserDetails userDetails) {
        Post post = postService.findById(id);
        User currentUser = userService.findByUsername(userDetails.getUsername());

        if (!post.getAuthor().getId().equals(currentUser.getId())) {
            return "redirect:/posts/" + id;
        }

        postService.deletePostById(id);
        return "redirect:/posts";
    }
    
    @PostMapping("/update/{id}")
    public String updatePost(@PathVariable Long id,
                             @ModelAttribute("post") Post updatedPost,
                             @AuthenticationPrincipal UserDetails userDetails,
                             RedirectAttributes redirectAttributes) {
        Post existingPost = postService.findById(id);
        if (existingPost == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bài viết không tồn tại.");
            return "redirect:/posts";
        }

        if (!existingPost.getAuthor().getUsername().equals(userDetails.getUsername())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bạn không có quyền sửa bài viết này.");
            return "redirect:/posts";
        }

        existingPost.setTitle(updatedPost.getTitle());
        existingPost.setContent(updatedPost.getContent());
        existingPost.setCategory(updatedPost.getCategory());
        postService.savePost(existingPost);

        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật bài viết thành công!");
        return "redirect:/posts/" + id;
    }



    @PostMapping("/{id}/comments")
    public String addComment(@PathVariable Long id, 
                             @RequestParam String content, 
                             @AuthenticationPrincipal UserDetails userDetails) {
        Post post = postService.findById(id);
        User user = userService.findByUsername(userDetails.getUsername()); 
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setUser(user); 
        comment.setPost(post);
        commentService.saveComment(comment);
        return "redirect:/posts/" + id;
    }

    @PostMapping("/comments/{commentId}/delete")
    public String deleteComment(@PathVariable Long commentId,
                                @AuthenticationPrincipal UserDetails userDetails) {
        Comment comment = commentService.findById(commentId);
        if (comment == null) {
            return "redirect:/posts"; 
        }

        String currentUsername = userDetails.getUsername();
        if (!comment.getUser().getUsername().equals(currentUsername)) {
            return "redirect:/posts/" + comment.getPost().getId(); 
        }

        commentService.deleteComment(commentId);
        return "redirect:/posts/" + comment.getPost().getId(); 
    }

    @PostMapping("/{postId}/comments/{commentId}/reply")
    public String replyComment(@PathVariable Long postId,
                               @PathVariable Long commentId,
                               @RequestParam String content,
                               @AuthenticationPrincipal UserDetails userDetails) {
        Post post = postService.findById(postId);
        Comment parentComment = commentService.findById(commentId);
        User user = userService.findByUsername(userDetails.getUsername());

        Comment reply = new Comment();
        reply.setContent(content);
        reply.setUser(user);
        reply.setPost(post);
        reply.setParent(parentComment);  
        commentService.saveComment(reply);

        return "redirect:/posts/" + postId;
    }

    @GetMapping("/create")
    public String showCreatePostForm(Model model, HttpServletRequest request) {
        model.addAttribute("post", new Post());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "posts/create";
    }

    @PostMapping("/create")
    public String createPost(@ModelAttribute("post") Post post,
                             @AuthenticationPrincipal UserDetails userDetails, 
                             RedirectAttributes redirectAttributes) {
        User user = userService.findByUsername(userDetails.getUsername());
        post.setAuthor(user);
        postService.savePost(post);
        redirectAttributes.addFlashAttribute("successMessage", "Tạo bài viết thành công!");
        return "redirect:/posts";
    }

    @GetMapping("/search")
    public String searchPosts(@RequestParam("keyword") String keyword, Model model) {
        List<Post> searchResults = postRepository.findByTitleContainingIgnoreCase(keyword);
        model.addAttribute("posts", searchResults);
        return "posts/index";
    }
    
    @GetMapping("/categories/{id}/posts")
    public String showPostsByCategory(@PathVariable("id") Long id, Model model) {
        List<Post> posts = postService.findByCategoryId(id);
        model.addAttribute("posts", posts);
        model.addAttribute("categoryId", id);
        model.addAttribute("categories", categoryService.getAllCategories()); 
        return "posts/index";
    } 
}

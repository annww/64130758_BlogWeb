package ntu.hongdta_64130758.controller;

import java.security.Principal;
import java.time.LocalDateTime;
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

import ntu.hongdta_64130758.models.Comment;
import ntu.hongdta_64130758.models.Post;
import ntu.hongdta_64130758.models.User;
import ntu.hongdta_64130758.services.interf.ICommentService;
import ntu.hongdta_64130758.services.interf.IPostService;
import ntu.hongdta_64130758.services.interf.IUserService;
@Controller
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private IPostService postService;

    @Autowired
    private ICommentService commentService;
    
    @Autowired
    private IUserService userService;
    
    @GetMapping
    public String showPostList(Model model) {
        List<Post> posts = postService.getAllPosts();
        model.addAttribute("posts", posts);
        return "posts/index"; 
    }
    @GetMapping("/{id}")
    public String showPostDetail(@PathVariable Long id, Model model) {
        Post post = postService.findById(id);
        List<Comment> comments = commentService.getCommentsByPostId(id);
        model.addAttribute("post", post);
        model.addAttribute("comments", comments);
        return "posts/detail"; 
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

 	
    @GetMapping("/create")
    public String showCreatePostForm(Model model) {
        model.addAttribute("post", new Post()); 
        return "posts/create"; 
    }

    @PostMapping("/create")
    public String createPost(@ModelAttribute("post") Post post) {
        postService.savePost(post);
        return "redirect:/posts"; 
    }
    
   

}

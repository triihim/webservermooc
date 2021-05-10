package projekti.api;

import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import projekti.DTO.AccountDTO;
import projekti.DTO.CommentDTO;
import projekti.DTO.FollowingDTO;
import projekti.DTO.PostDTO;
import projekti.DTO.PostLikeResponseDTO;
import projekti.helpers.SecurityHelper;
import projekti.models.Post;
import projekti.services.AccountService;
import projekti.services.CommentService;
import projekti.services.FollowingService;
import projekti.services.PostService;

@RestController
@RequestMapping("/api")
public class AjaxController {
    
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private PostService postService;
    
    @Autowired
    private FollowingService followingService;
    
    @Autowired
    private CommentService commentService;
    
    @Autowired
    private TemplateEngine templateEngine;
    
    @PostMapping("/posts")
    public Post createPost(@RequestBody PostDTO dto) {
        if(dto.getContent() == null || dto.getContent().length() < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Post content empty");
        }
        
        return postService.createPost(dto);
    }
    
    @PostMapping("/posts/{id}/like")
    public PostLikeResponseDTO likePost(@PathVariable Long id) {
        int likeCount = postService.likePost(id);
        return new PostLikeResponseDTO(id, SecurityHelper.requesterUsername(), likeCount);
    }
    
    @PostMapping("/toggle-follow/{username}") 
    public FollowingDTO toggleFollowing(@PathVariable String username) {
        return followingService.toggleFollowing(username);
    }
    
    @GetMapping("/follow-status/{username}")
    public FollowingDTO getFollowingStatus(@PathVariable String username) {
        return followingService.getFollowingStatus(username);
    }
    
    @GetMapping("/feed/{username}")
    public List<PostDTO> getFeed(@PathVariable String username) {
        return postService.getUserFeed(username);
    }
    
    @RequestMapping(
        value = "/feed/{username}/html", 
        method = RequestMethod.GET, 
        produces = "text/html"
    )
    @ResponseBody
    public String getFeedAsHTML(@PathVariable String username, HttpServletRequest request, HttpServletResponse response) {
        List<PostDTO> feed = postService.getUserFeed(username);
        WebContext ctx = new WebContext(request, response, request.getServletContext());
        ctx.setVariable("feed", feed);
        return templateEngine.process("feed", ctx);
    }
    
    @PostMapping("/posts/{id}/comment")
    public CommentDTO commentPost(@PathVariable Long id, @RequestBody CommentDTO dto) {
        if(dto.getContent().length() < 1 || dto.getContent().length() > 60) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Comment length is limited to 1-60 characters");
        }
        return commentService.comment(id, dto.getContent());
    }
    
    @GetMapping("/followers/{username}")
    public List<AccountDTO> getFollowers(@PathVariable String username) {
        return followingService.getFollowers(username);
    }
    
    @RequestMapping(
        value = "/followers/{username}/html", 
        method = RequestMethod.GET, 
        produces = "text/html"
    )
    @ResponseBody
    public String getFollowers(@PathVariable String username, HttpServletRequest request, HttpServletResponse response) {
        List<AccountDTO> accounts =  followingService.getFollowers(username);
        WebContext ctx = new WebContext(request, response, request.getServletContext());
        ctx.setVariable("users", accounts);
        return templateEngine.process("followlist", ctx);
    }
    
    @RequestMapping(
        value = "/followees/{username}/html", 
        method = RequestMethod.GET, 
        produces = "text/html"
    )
    @ResponseBody
    public String getFollowees(@PathVariable String username, HttpServletRequest request, HttpServletResponse response) {
        List<AccountDTO> accounts =  followingService.getFollowees(username);
        WebContext ctx = new WebContext(request, response, request.getServletContext());
        ctx.setVariable("users", accounts);
        return templateEngine.process("followlist", ctx);
    }
}

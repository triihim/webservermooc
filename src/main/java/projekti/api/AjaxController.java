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
import org.springframework.web.server.ResponseStatusException;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import projekti.DTO.AccountDTO;
import projekti.DTO.FollowingDTO;
import projekti.DTO.PostDTO;
import projekti.DTO.PostLikeResponseDTO;
import projekti.helpers.SecurityHelper;
import projekti.models.Post;
import projekti.services.AccountService;
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
    private TemplateEngine templateEngine;
    
    @GetMapping("/accounts")
    public List<AccountDTO> accounts(@RequestParam Optional<String> filter) {
        return accountService.getAccountNamesContaining(filter.orElse(""));
    }
    
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
        return postService.getFeed(username);
    }
    
    @RequestMapping(
        value = "/feed/{username}/html", 
        method = RequestMethod.GET, 
        produces = "text/html"
    )
    @ResponseBody
    public String getFeedAsHTML(@PathVariable String username, HttpServletRequest request, HttpServletResponse response) {
        List<PostDTO> feed = postService.getFeed(username);
        WebContext ctx = new WebContext(request, response, request.getServletContext());
        ctx.setVariable("feed", feed);
        return templateEngine.process("feed", ctx);
    }
    
}

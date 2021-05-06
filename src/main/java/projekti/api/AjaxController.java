package projekti.api;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import projekti.DTO.FollowingDTO;
import projekti.DTO.PostDTO;
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
    
    @GetMapping("/accounts")
    public List<String> accounts(@RequestParam Optional<String> filter) {
        return accountService.getAccountNamesContaining(filter.orElse(""));
    }
    
    @PostMapping("/posts")
    public Post createPost(@RequestBody PostDTO dto) {
        if(dto.getContent() == null || dto.getContent().length() < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Post content empty");
        }
        
        Post createdPost = postService.createPost(dto);
        
        return createdPost;
    }
    
    @PostMapping("/toggle-follow/{username}") 
    public FollowingDTO toggleFollowing(@PathVariable String username) {
        return followingService.toggleFollowing(username);
    }
    
    @GetMapping("/follow-status/{username}")
    public FollowingDTO getFollowingStatus(@PathVariable String username) {
        return followingService.getFollowingStatus(username);
    }
    
    @GetMapping("/feed")
    public List<PostDTO> getFeed() {
        return postService.getRequesterFeed();
    }
    
}

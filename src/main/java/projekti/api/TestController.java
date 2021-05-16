package projekti.api;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import projekti.DTO.PostDTO;
import projekti.services.FollowingService;
import projekti.services.PostService;

/**
 * API-controller that is used to support testing
 * since some endpoints return server-side rendered HTML
 * which is difficult to test.
 */
@Profile({"test", "development"})
@RestController("apiTestController")
@RequestMapping("/api/test")
public class TestController {
    
    @Autowired
    private PostService postService;
    
    @Autowired
    private FollowingService followingService;
    
    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
    
    @GetMapping("/feed/{username}")
    public List<PostDTO> getUserFeed(@PathVariable String username) {
        return postService.getUserFeed(username);
    }
    
    
}

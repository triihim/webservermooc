package projekti.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import projekti.DTO.FollowingDTO;
import projekti.DTO.FollowBlockDTO;
import projekti.services.FollowingService;

@RestController
@RequestMapping("/api/following")
public class FollowingController {
    
    @Autowired
    private FollowingService followingService;

    @RequestMapping(value = "/toggle-follow/{username}", method = {RequestMethod.POST}) 
    public FollowingDTO toggleFollowing(@PathVariable String username) {
        return followingService.toggleFollowing(username);
    }
    
    @RequestMapping(value = "/toggle-block/{userId}", method = {RequestMethod.POST})
    public FollowBlockDTO toggleBlock(@PathVariable Long userId) {
        return followingService.toggleBlock(userId);
    }
    
    @GetMapping("/status/{username}")
    public FollowingDTO getFollowingStatus(@PathVariable String username) {
        return followingService.getFollowingStatus(username);
    }
    
}

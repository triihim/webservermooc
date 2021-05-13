package projekti.api;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import projekti.DTO.AccountDTO;
import projekti.DTO.CommentDTO;
import projekti.DTO.FollowBlockDTO;
import projekti.DTO.FollowListItemDTO;
import projekti.DTO.FollowingDTO;
import projekti.DTO.PhotoDTO;
import projekti.DTO.PhotoLikeResponseDTO;
import projekti.DTO.PostDTO;
import projekti.DTO.PostLikeResponseDTO;
import projekti.helpers.SecurityHelper;
import projekti.models.Post;
import projekti.services.CommentService;
import projekti.services.FollowingService;
import projekti.services.PhotoService;
import projekti.services.PostService;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class AjaxController {
    
    @Autowired
    private PostService postService;
    
    @Autowired
    private FollowingService followingService;
    
    @Autowired
    private CommentService commentService;
    
    @Autowired
    private PhotoService photoService;
    
    @Autowired
    private TemplateEngine templateEngine;
    
    @RequestMapping(value = "/posts", method = {RequestMethod.POST})
    public Post createPost(@RequestBody PostDTO dto) {
        if(dto.getContent() == null || dto.getContent().length() < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Post content empty");
        }
        
        return postService.createPost(dto);
    }
    
    @RequestMapping(value = "/posts/{id}/like", method = {RequestMethod.POST})
    public PostLikeResponseDTO likePost(@PathVariable Long id) {
        int likeCount = postService.likePost(id);
        return new PostLikeResponseDTO(id, SecurityHelper.requesterUsername(), likeCount);
    }
    
    @RequestMapping(value = "/toggle-follow/{username}", method = {RequestMethod.POST}) 
    public FollowingDTO toggleFollowing(@PathVariable String username) {
        return followingService.toggleFollowing(username);
    }
    
    @RequestMapping(value = "/toggle-block/{userId}", method = {RequestMethod.POST})
    public FollowBlockDTO toggleBlock(@PathVariable Long userId) {
        return followingService.toggleFollowing(userId);
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
        ctx.setVariable("feedOwner", username);
        return templateEngine.process("feed", ctx);
    }
    
    @RequestMapping(value = "/posts/{id}/comment", method = {RequestMethod.POST})
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
        List<FollowListItemDTO> followers =  followingService.getFollowersListing(username);
        WebContext ctx = new WebContext(request, response, request.getServletContext());
        ctx.setVariable("users", followers);
        return templateEngine.process("followlist", ctx);
    }
    
    @RequestMapping(
        value = "/followees/{username}/html", 
        method = RequestMethod.GET, 
        produces = "text/html"
    )
    @ResponseBody
    public String getFollowees(@PathVariable String username, HttpServletRequest request, HttpServletResponse response) {
        List<FollowListItemDTO> followees =  followingService.getFolloweesListing(username);
        WebContext ctx = new WebContext(request, response, request.getServletContext());
        ctx.setVariable("users", followees);
        return templateEngine.process("followlist", ctx);
    }
    
     @RequestMapping(
        value = "/photos/{username}/html", 
        method = RequestMethod.GET, 
        produces = "text/html"
    )
    @ResponseBody
    public String getPhotos(@PathVariable String username, HttpServletRequest request, HttpServletResponse response) {
        List<PhotoDTO> photos = photoService.getUserPhotos(username);
        WebContext ctx = new WebContext(request, response, request.getServletContext());
        ctx.setVariable("owner", SecurityHelper.accessorIsLoggedInUser(username));
        ctx.setVariable("photos", photos);
        ctx.setVariable("maxPhotosReached", photoService.isMaxPhotosReached(username));
        return templateEngine.process("photos", ctx);
    }
    
    @DeleteMapping("/photos/{id}")
    public Long deletePhoto(@PathVariable Long id) {
        return photoService.deletePhoto(id);
    }
    
    @PostMapping("/photos/{id}/like")
    public PhotoLikeResponseDTO likePhoto(@PathVariable("id") Long photoId) {
        return photoService.likePhoto(photoId);
    }
}

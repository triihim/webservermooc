package projekti.api;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import projekti.DTO.FollowListItemDTO;
import projekti.DTO.PhotoDTO;
import projekti.DTO.PostDTO;
import projekti.helpers.SecurityHelper;
import projekti.services.FollowingService;
import projekti.services.PhotoService;
import projekti.services.PostService;

@RestController
@RequestMapping("/api/html")
public class HTMLController {
    
    @Autowired
    private TemplateEngine templateEngine;
    
    @Value("${spring.profiles.active}")
    private String activeProfile;  
    
    @Autowired
    private PostService postService;
    
    @Autowired
    private FollowingService followingService;
    
    @Autowired
    private PhotoService photoService;
    
    @RequestMapping(value = "/feed/{username}", method = RequestMethod.GET, produces = "text/html")
    @ResponseBody
    public String getFeedPartial(@PathVariable String username, HttpServletRequest request, HttpServletResponse response) {
        List<PostDTO> feed = postService.getUserFeed(username);
        WebContext ctx = new WebContext(request, response, request.getServletContext());
        ctx.setVariable("feed", feed);
        ctx.setVariable("feedOwner", username);
        return templateEngine.process("feed", ctx);
    }
    
    @RequestMapping(value = "/followers/{username}", method = RequestMethod.GET, produces = "text/html")
    @ResponseBody
    public String getFollowersPartial(@PathVariable String username, HttpServletRequest request, HttpServletResponse response) {
        List<FollowListItemDTO> followers =  followingService.getFollowersListing(username);
        WebContext ctx = new WebContext(request, response, request.getServletContext());
        ctx.setVariable("users", followers);
        return templateEngine.process("followlist", ctx);
    }
    
    @RequestMapping(value = "/followees/{username}", method = RequestMethod.GET, produces = "text/html")
    @ResponseBody
    public String getFolloweesPartial(@PathVariable String username, HttpServletRequest request, HttpServletResponse response) {
        List<FollowListItemDTO> followees =  followingService.getFolloweesListing(username);
        WebContext ctx = new WebContext(request, response, request.getServletContext());
        ctx.setVariable("users", followees);
        return templateEngine.process("followlist", ctx);
    }
    
    @RequestMapping(value = "/photos/{username}", method = RequestMethod.GET, produces = "text/html")
    @ResponseBody
    public String getPhotosPartial(@PathVariable String username, HttpServletRequest request, HttpServletResponse response) {
        List<PhotoDTO> photos = photoService.getUserPhotos(username);
        WebContext ctx = new WebContext(request, response, request.getServletContext());
        ctx.setVariable("owner", SecurityHelper.accessorIsLoggedInUser(username));
        ctx.setVariable("photos", photos);
        ctx.setVariable("includeCsrf", activeProfile.equals("production"));
        ctx.setVariable("maxPhotosReached", photoService.isMaxPhotosReached(username));
        return templateEngine.process("photos", ctx);
    }
}

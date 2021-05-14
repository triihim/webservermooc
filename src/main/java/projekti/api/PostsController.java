package projekti.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;
import projekti.DTO.CommentDTO;
import projekti.DTO.PostDTO;
import projekti.DTO.PostLikeResponseDTO;
import projekti.helpers.SecurityHelper;
import projekti.models.Post;
import projekti.services.CommentService;
import projekti.services.PostService;

@RestController
@RequestMapping("/api/posts")
public class PostsController {
    
    @Autowired
    private PostService postService;
    
    @Autowired
    private CommentService commentService;
    
    @RequestMapping(value = "/", method = {RequestMethod.POST})
    public Post createPost(@RequestBody PostDTO dto) {
        if(dto.getContent() == null || dto.getContent().length() < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Post content empty");
        }
        
        return postService.createPost(dto);
    }
    
    @RequestMapping(value = "/{id}/like", method = {RequestMethod.POST})
    public PostLikeResponseDTO likePost(@PathVariable Long id) {
        int likeCount = postService.likePost(id);
        return new PostLikeResponseDTO(id, SecurityHelper.requesterUsername(), likeCount);
    }
    
    @RequestMapping(value = "/{id}/comment", method = {RequestMethod.POST})
    public CommentDTO commentPost(@PathVariable Long id, @RequestBody CommentDTO dto) {
        if(dto.getContent().length() < 1 || dto.getContent().length() > 60) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Comment length is limited to 1-60 characters");
        }
        return commentService.commentPost(id, dto.getContent());
    }
    
}

package projekti.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import projekti.DTO.CommentDTO;
import projekti.DTO.PhotoLikeResponseDTO;
import projekti.services.CommentService;
import projekti.services.PhotoService;

@RestController
@RequestMapping("/api/photos")
public class PhotosController {
    
    @Autowired
    private CommentService commentService;
    
    @Autowired
    private PhotoService photoService;
    
    @RequestMapping(value = "/{id}/comment", method = {RequestMethod.POST})
    public CommentDTO commentPhoto(@PathVariable Long id, @RequestBody CommentDTO dto) {
        if(dto.getContent().length() < 1 || dto.getContent().length() > 60) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Comment length is limited to 1-60 characters");
        }
        return commentService.commentPhoto(id, dto.getContent());
    }
    
    @DeleteMapping("/{id}")
    public Long deletePhoto(@PathVariable Long id) {
        return photoService.deletePhoto(id);
    }
    
    @PostMapping("/{id}/like")
    public PhotoLikeResponseDTO likePhoto(@PathVariable("id") Long photoId) {
        return photoService.likePhoto(photoId);
    }
}

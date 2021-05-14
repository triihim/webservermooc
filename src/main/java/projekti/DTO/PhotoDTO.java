package projekti.DTO;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhotoDTO {
    
    private long id;
    
    private String description;
    
    private long likes;
    
    private boolean canLike;
    
    private boolean canComment;
    
    private boolean isProfilePicture;
    
    private List<CommentDTO> comments;

}

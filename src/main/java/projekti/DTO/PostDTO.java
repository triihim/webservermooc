package projekti.DTO;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PostDTO {
    
    private long id;
    
    private String content;
    
    private LocalDateTime createdAt;
    
    private String owner;
    
    private long likes;
    
    private boolean canLike;
    
    private boolean canComment;
    
    private List<CommentDTO> comments;
    
}

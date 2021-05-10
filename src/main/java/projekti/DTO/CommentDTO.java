package projekti.DTO;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    
    private long id;
    
    private long postId;
    
    private String content;
    
    private String author;
    
    private LocalDateTime createdAt;
    
}

package projekti.DTO;

import java.time.LocalDateTime;
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
    
    private int likes;
    
    private boolean canLike;
    
}

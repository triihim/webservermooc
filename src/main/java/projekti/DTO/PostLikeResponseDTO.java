package projekti.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostLikeResponseDTO {
    
    private long postId;
    
    private String liker;
    
    private int likes;
    
}

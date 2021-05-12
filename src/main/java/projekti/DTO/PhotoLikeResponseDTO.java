package projekti.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhotoLikeResponseDTO {
    
    private long photoId;
    
    private String liker;
    
    private int likes;
    
}
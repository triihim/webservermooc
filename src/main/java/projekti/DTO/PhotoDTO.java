package projekti.DTO;

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
    
    private boolean isProfilePicture;

}

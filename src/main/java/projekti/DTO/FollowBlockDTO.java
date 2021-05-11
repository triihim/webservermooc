package projekti.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowBlockDTO {
    
    private long followerId;
    
    private String followerUsername;
    
    private boolean isBlocked;
    
}

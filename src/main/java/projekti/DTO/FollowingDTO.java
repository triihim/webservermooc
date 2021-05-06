package projekti.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowingDTO {
    
    private boolean isFollowing;
    
    private String followee;
    
    private int followerCount;
    
}

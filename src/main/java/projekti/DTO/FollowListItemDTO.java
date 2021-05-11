package projekti.DTO;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowListItemDTO {
    
    private long userId;
    
    private String username;
    
    private String firstName;
    
    private String lastName;
    
    private LocalDateTime startedAt;
    
    private Boolean isBlocked;
    
}

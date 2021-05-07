package projekti.projections;

import java.time.LocalDateTime;

public interface IUserFeedItem {
    
    long getId();
    
    String getContent();
    
    LocalDateTime getCreatedAt();
    
    String getOwner();
    
    int getLikes();
    
}

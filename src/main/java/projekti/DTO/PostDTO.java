package projekti.DTO;

import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PostDTO {
    
    private String content;
    
    private ZonedDateTime createdAt;
    
    private String owner;
    
}

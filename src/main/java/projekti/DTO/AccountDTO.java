package projekti.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AccountDTO {
    
    private String firstName;
    
    private String lastName;
    
    private String username;

    private Long profilePictureId;
}

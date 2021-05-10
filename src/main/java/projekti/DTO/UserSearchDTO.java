package projekti.DTO;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSearchDTO {

    private List<AccountDTO> users;
    
    private String query;
    
    private long currentPage;
    
    private long totalPages;
    
}

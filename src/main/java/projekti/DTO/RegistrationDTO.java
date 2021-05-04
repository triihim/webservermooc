package projekti.DTO;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RegistrationDTO {
    
    @NotEmpty
    @Size(min = 2, max = 50)
    private String firstName;
    
    @NotEmpty
    @Size(min = 2, max = 50)
    private String lastName;
    
    @NotEmpty
    @Size(min = 3, max = 20)
    private String username;
    
    @NotEmpty
    @Size(min = 8, max = 50)
    private String password;

    @NotEmpty
    @Size(min = 8, max = 50)
    private String passwordConfirm;
    
    public boolean passwordsMatch() {
        return password != null && password.equals(passwordConfirm);
    }

}

package projekti.DTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RegistrationDTO {
    
    @NotBlank
    @NotEmpty
    @Size(min = 2, max = 50)
    private String firstName;
    
    @NotBlank
    @NotEmpty
    @Size(min = 2, max = 50)
    private String lastName;
    
    @NotBlank
    @NotEmpty
    @Size(min = 3, max = 20)
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "No special characters allowed")
    private String username;
    
    @NotBlank
    @NotEmpty
    @Size(min = 8, max = 50)
    private String password;

    @NotBlank
    @NotEmpty
    @Size(min = 8, max = 50)
    private String passwordConfirm;
    
    public boolean passwordsMatch() {
        return password != null && password.equals(passwordConfirm);
    }

}

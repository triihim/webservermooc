package projekti.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such account")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountNotFoundException extends RuntimeException {
    
    private String message;
    
}

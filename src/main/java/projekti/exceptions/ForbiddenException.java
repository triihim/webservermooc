package projekti.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Forbidden")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForbiddenException extends RuntimeException {
    
    private String message;
    
}

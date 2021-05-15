package projekti.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Photo limit reached")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhotoLimitReachedException extends RuntimeException {
    
    public String message;
    
}

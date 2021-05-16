package projekti.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE, reason = "Wrong format")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhotoFormatException extends RuntimeException {
    
    public String message;
    
}

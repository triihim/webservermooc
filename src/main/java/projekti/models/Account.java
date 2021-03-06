package projekti.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.Column;
import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Account extends AbstractPersistable<Long> {
    
    private String firstName;
    
    private String lastName;
    
    @Column(unique = true)
    private String username;
    
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // For situations where entity is returned as is.
    private String password;
    
    private Long profilePictureId;

}

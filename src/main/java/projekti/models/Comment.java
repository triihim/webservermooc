package projekti.models;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Comment extends AbstractPersistable<Long> {
    
    @Size(min = 1, max = 60)
    private String content;
    
    @ManyToOne(fetch = FetchType.EAGER)
    private Account owner;
    
    private LocalDateTime createdAt;
    

}

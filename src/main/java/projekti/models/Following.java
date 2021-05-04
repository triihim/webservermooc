package projekti.models;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Following extends AbstractPersistable<Long> {
    
    @OneToOne
    private Account follower;
    
    private boolean isBlocked;
    
    private LocalDateTime startedAt;
}

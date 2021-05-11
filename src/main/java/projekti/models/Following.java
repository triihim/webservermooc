package projekti.models;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Following extends AbstractPersistable<Long> {
    
    @ManyToOne(fetch = FetchType.EAGER)
    private Account follower;
    
    @ManyToOne(fetch = FetchType.EAGER)
    private Account followee;
    
    private boolean isFollowerBlocked;
    
    private LocalDateTime startedAt;
}

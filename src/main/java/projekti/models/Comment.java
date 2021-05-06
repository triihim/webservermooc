package projekti.models;

import java.time.ZonedDateTime;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Comment extends AbstractPersistable<Long> {
    
    private String content;
    
    @ManyToOne
    private Account owner;
    
    private ZonedDateTime createdAt;
    
    @OneToMany
    private List<ResourceLike> likes;
}

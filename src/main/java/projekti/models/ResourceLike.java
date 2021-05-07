package projekti.models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import lombok.Data;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
public class ResourceLike extends AbstractPersistable<Long> {
    
    @ManyToOne
    private Account owner;
    
}

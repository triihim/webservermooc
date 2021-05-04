package projekti.models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class ResourceLike extends AbstractPersistable<Long> {
    
    @ManyToOne
    private Account owner;
    
}

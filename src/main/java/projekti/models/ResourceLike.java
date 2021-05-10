package projekti.models;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import lombok.Data;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
public class ResourceLike extends AbstractPersistable<Long> {
    
    @ManyToOne(fetch = FetchType.EAGER)
    private Account owner;
    
}

package projekti.models;

import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
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
public class Photo extends AbstractPersistable<Long> {
    
    @ManyToOne
    private Account owner;
    
    private String description;
    
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] content;
    
    @OneToMany
    private List<ResourceLike> likes;
}

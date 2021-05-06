package projekti.models;

import java.time.ZonedDateTime;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Post extends AbstractPersistable<Long> {
    
    private String content;
    
    @ManyToOne
    private Account owner;
    
    @Setter(AccessLevel.NONE)
    private String ownerUsername;

    private ZonedDateTime createdAt;
    
    @OneToMany
    private List<ResourceLike> likes;
    
    @OneToMany
    private List<Comment> comments;
    
    public void setOwner(Account a) {
        this.owner = a;
        this.ownerUsername = a.getUsername();
    }
}

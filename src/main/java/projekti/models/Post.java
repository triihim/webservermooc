package projekti.models;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;
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

    @Size(min = 1, max = 120)
    private String content;
    
    @ManyToOne
    private Account owner;
    
    @Setter(AccessLevel.NONE)
    private String ownerUsername;

    private LocalDateTime createdAt;
    
    @OneToMany
    private List<ResourceLike> likes;
    
    @OneToMany
    private List<Comment> comments;
    
    private long likeCount;
    
    public void setOwner(Account a) {
        this.owner = a;
        this.ownerUsername = a.getUsername();
    }
}

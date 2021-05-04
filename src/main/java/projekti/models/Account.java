package projekti.models;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Account extends AbstractPersistable<Long> {
    
    private String firstName;
    
    private String lastName;
    
    @Column(unique = true)
    private String username;
    
    private String password;
    
    @OneToOne
    private Photo profilePicture;
    
    @OneToMany
    private List<Following> followings;
    
    public Long getProfilePictureId() {
        return profilePicture != null ? profilePicture.getId() : null;
    }
}

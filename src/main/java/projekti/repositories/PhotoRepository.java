package projekti.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projekti.models.Photo;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    
    @EntityGraph(attributePaths = {"content"})
    public Photo findWithContentById(long id);
    
    @EntityGraph(attributePaths = {"likes"})
    public List<Photo> findAllWithLikesByOwner_usernameIgnoreCase(String username);
    
    @EntityGraph(attributePaths = {"likes"})
    public Photo findWithLikesById(long id);
    
    public long countByOwner_usernameIgnoreCase(String username);
    
    @EntityGraph(attributePaths = {"owner"})
    public Photo findWithOwnerById(long id);
}
